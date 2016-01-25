package com.raelifin.coi;

import com.min3d.core.Object3dContainer;
import com.min3d.objectPrimitives.Box;
import com.min3d.vos.TextureVo;

/**
 * Created by HP on 8/18/2015.
 */
public class MapNodeView implements MapNodeListener {

    public Object3dContainer o;

    public MapNodeView(Object3dContainer o) {
        this.o = o;
    }

    @Override
    public void changeTexture(String textureName) {
        System.out.println(textureName);
        System.out.println(o);
        System.out.println(o.textures().size());
        o.textures().addReplace(new TextureVo(textureName));
    }
}
