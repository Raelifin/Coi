package com.min3d.parser;

import android.content.res.Resources;

/**
 * Parser factory class. Specify the parser type and the corresponding
 * specialized class will be returned.
 * @author dennis.ippel
 *
 */
public class Parser {
	/**
	 * Parser types enum
	 * @author dennis.ippel
	 *
	 */
	public static enum Type { OBJ, MAX_3DS, MD2, PACKAGE_NAME };
	
	/**
	 * Create a parser of the specified type.
	 * @param type
	 * @param resources
	 * @param resourceID
	 * @return
	 */
	public static IParser createParser(Type type, Resources resources, String resourceID, boolean generateMipMap, String packageID)
	{
		switch(type)
		{
			case OBJ:
				return new ObjParser(resources, resourceID, generateMipMap, packageID);
			case MAX_3DS:
				return new Max3DSParser(resources, resourceID, generateMipMap, packageID);
			case MD2:
				return new MD2Parser(resources, resourceID, generateMipMap, packageID);
		}
		
		return null;
	}
}
