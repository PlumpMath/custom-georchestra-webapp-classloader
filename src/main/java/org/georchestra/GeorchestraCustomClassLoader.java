package org.georchestra;

import org.apache.catalina.loader.WebappClassLoader;

public class GeorchestraCustomClassLoader extends WebappClassLoader {

    protected static final String[] triggers = {
        "javax.servlet.Servlet",                     // Servlet API
        "org.geotools.data.ogr.jni.JniOGRDataStoreFactory", // GeoTools OGR
        "it.geosolutions.imageio.gdalframework.GDALUtilities" // Gdal utilities from imageio-ext
    };
    public GeorchestraCustomClassLoader() {
        super();
    }

    public GeorchestraCustomClassLoader(ClassLoader parent) {
        super(parent);
    }
}
