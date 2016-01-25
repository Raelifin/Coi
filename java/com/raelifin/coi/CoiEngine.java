package com.raelifin.coi;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.min3d.Shared;
import com.min3d.Utils;
import com.min3d.core.Object3dContainer;
import com.min3d.core.RendererActivity;
import com.min3d.objectPrimitives.Box;
import com.min3d.parser.IParser;
import com.min3d.vos.Color4;
import com.min3d.vos.Light;
import com.min3d.vos.Number3d;
import com.min3d.vos.TextureVo;

import java.io.IOException;
import java.util.ArrayList;

import edu.cmu.pocketsphinx.Assets;

import static android.widget.Toast.makeText;

public class CoiEngine extends RendererActivity implements VoiceListener {

    public static final int CELL_SIZE = 4;

    private long prevTime = 0;

    private Map map = new Map(LevelSpecs.buildLevel1());
    private VoiceManager voice;

    public DialogueLog dialogLog = new DialogueLog();

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        Jukebox.init(this);

        makeText(getApplicationContext(), "Preparing the recognizer", Toast.LENGTH_SHORT).show();
        try {
            voice = new VoiceManager(this, new Assets(CoiEngine.this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreateSetContentView() {
        setContentView(R.layout.coi_game_layout);

        ((LinearLayout) this.findViewById(R.id.scene2Holder)).addView(_glSurfaceView);

        ((Button) this.findViewById(R.id.LeftButton)).setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                map.player.turn(true);
                return true;
            }
        });
        ((Button) this.findViewById(R.id.RightButton)).setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                map.player.turn(false);
                return true;
            }
        });
        ((Button) this.findViewById(R.id.ForwardButton)).setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                map.player.walk();
                return true;
            }
        });

        ImageButton speakButton = (ImageButton) this.findViewById(R.id.SpeakButton);
        speakButton.setEnabled(false);
        speakButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    Jukebox.play("click");
                    voice.recognizer.startListening("normal");
                    v.setPressed(true);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    voice.recognizer.stop();
                    Jukebox.play("click");
                    v.setPressed(false);
                }
                return true;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        voice.destroy();
    }

    private Object3dContainer head;

	public void initScene() {
        Bitmapbox.init(this);

        Parserbox.init(this);

        buildLevel();
	}

    private void buildLevel() {
        scene.reset();

        for (MapNode n : map.mapNodes) {
            Object3dContainer o;
            if (n instanceof Head) {
                Head h = (Head)n;
                scene.addChild(makeObj((int)(n.x), (int)(n.y), (int)(n.z), (float)n.rotation, n.modelName));
                o = makeHead((int) (n.x), (int) (n.y), (int) (n.z), (float) n.rotation, h.getTexture());
            } else {
                o = makeObj((int)(n.x), -1, (int)(n.z), (float)n.rotation, n.modelName);
            }
            n.setListener(new MapNodeView(o));
            scene.addChild(o);
        }

        for (MapEntity e : map.decorations) {
            Object3dContainer o = makeObj((int)(e.x), (int)(e.y), (int)(e.z), (float)e.rotation, e.modelName);
            scene.addChild(o);
        }

        scene.fogColor(new Color4(0, 0, 0, 255));
        scene.fogNear(0);
        scene.fogFar(4 * CELL_SIZE);
        scene.fogEnabled(true);

        Light l1 = new Light();
        l1.position.setAll(0, 1, 0);
        l1.ambient.setAll(255, 255, 255, 255);
        scene.lights().add(l1);

        prevTime = System.currentTimeMillis();
    }

    private Object3dContainer makeObj(float x, float y, float z, double rotation, String parser) {
        System.out.println("Loading: "+parser);
        IParser p = Parserbox.get(parser);
        p.parse();
        Object3dContainer r = p.getParsedObject();
        r.position().setAll(x, y, z);
        if (rotation > 0) {
            r.getChildAt(r.numChildren()-1).rotation().y = (float) (rotation*180/Math.PI);
        }
        return r;
    }

    public Object3dContainer makeHead(float x, float y, float z, double rotation, String defaultTexture) {
        Object3dContainer r = new Box(0.9f,0.9f,0.9f, new Color4(255, 255, 255, 255));
        r.lightingEnabled(false);
        r.textures().add(new TextureVo(defaultTexture));
        r.position().setAll(x, y, z);
        if (rotation > 0) {
            r.rotation().y = (float) (rotation*180/Math.PI);
        }
        return r;
    }

	@Override 
	public void updateScene() {
        int dt = (int)(System.currentTimeMillis() - prevTime);
        prevTime = System.currentTimeMillis();

        if (map == null) { return; }
        map.update(dt, dialogLog);

        Number3d p = map.player.getPos();
        double theta = map.player.getTheta();
        scene.camera().position.setAllFrom(p);
        scene.camera().target.setAll((float) (p.x + Math.cos(theta)), p.y, (float) (p.z + Math.sin(theta)));

        if (map.nextLevelTimer >= 0) {
            //TODO: Refactor
            map.nextLevelTimer -= dt;
            if (map.nextLevelTimer < 0) {
                map = new Map(LevelSpecs.buildLevel2());
                buildLevel();
            }
        }
	}

    @Override
    public void onUpdateScene() {
        ((Button) this.findViewById(R.id.LeftButton)).setEnabled(map.player.canTurn(true));
        ((Button) this.findViewById(R.id.RightButton)).setEnabled(map.player.canTurn(false));
        ((Button) this.findViewById(R.id.ForwardButton)).setEnabled(map.player.canAdvance());
        if (dialogLog.needsRepaint()) {
            DialogueTextView textView = ((DialogueTextView) this.findViewById(R.id.mainDialogueView));
            textView.text = dialogLog.getLogCopy();
            textView.invalidate();
        }
    }

    @Override
    public void voiceError(String message) {
        makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void hearSpeech(String message) {
        dialogLog.add("Player:"+message);
        map.player.say(message, dialogLog);
    }

    @Override
    public void voiceReady() {
        ImageButton speakButton = (ImageButton) CoiEngine.this.findViewById(R.id.SpeakButton);
        speakButton.setEnabled(true);
    }
}
