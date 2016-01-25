package com.raelifin.coi;

import android.content.Context;
import android.content.res.Resources;

import com.min3d.parser.IParser;
import com.min3d.parser.Parser;

import java.util.HashMap;

/**
 * Created by HP on 8/15/2015.
 */
public class Parserbox {

    private static HashMap<String,IParser> map = new HashMap<String,IParser>();

    public static void init(Context context) {
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        map.put("0Platform", Parser.createParser(Parser.Type.MAX_3DS, resources, "raw/zero", false, packageName));
        map.put("1Platform", Parser.createParser(Parser.Type.MAX_3DS, resources, "raw/one", false, packageName));
        map.put("2Platform", Parser.createParser(Parser.Type.MAX_3DS, resources, "raw/two", false, packageName));
        map.put("straight2Platform", Parser.createParser(Parser.Type.MAX_3DS, resources, "raw/two2", false, packageName));
        map.put("3Platform", Parser.createParser(Parser.Type.MAX_3DS, resources, "raw/three", false, packageName));
        map.put("4Platform", Parser.createParser(Parser.Type.MAX_3DS, resources, "raw/four", false, packageName));
        map.put("head", Parser.createParser(Parser.Type.MAX_3DS, resources, "raw/head", false, packageName));
        map.put("pillar", Parser.createParser(Parser.Type.OBJ, resources, "raw/pillar_obj", true, packageName));
        map.put("straight2BluePlatform", Parser.createParser(Parser.Type.OBJ, resources, "raw/two2blue_obj", false, packageName));
        map.put("2BluePlatform", Parser.createParser(Parser.Type.OBJ, resources, "raw/twoblue_obj", false, packageName));
        map.put("1BluePlatform", Parser.createParser(Parser.Type.OBJ, resources, "raw/oneblue_obj", false, packageName));
        map.put("straight2RedPlatform", Parser.createParser(Parser.Type.OBJ, resources, "raw/two2red_obj", false, packageName));
        map.put("2RedPlatform", Parser.createParser(Parser.Type.OBJ, resources, "raw/twored_obj", false, packageName));
        map.put("1RedPlatform", Parser.createParser(Parser.Type.OBJ, resources, "raw/onered_obj", false, packageName));
        map.put("straight2GreenPlatform", Parser.createParser(Parser.Type.OBJ, resources, "raw/two2green_obj", false, packageName));
        map.put("2GreenPlatform", Parser.createParser(Parser.Type.OBJ, resources, "raw/twogreen_obj", false, packageName));
        map.put("1GreenPlatform", Parser.createParser(Parser.Type.OBJ, resources, "raw/onegreen_obj", false, packageName));
    }

    public static IParser get(String parser) {
        return map.get(parser);
    }
}
