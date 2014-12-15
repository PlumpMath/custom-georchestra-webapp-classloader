package org.georchestra;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.catalina.loader.WebappClassLoader;

public class GeorchestraCustomClassLoader extends WebappClassLoader {


    protected static final String[] georTriggers = {
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

    /**
     * Check the specified JAR file, and return <code>true</code> if it does
     * not contain any of the trigger classes.
     *
     * @param jarfile The JAR file to be checked
     *
     * @exception IOException if an input/output error occurs
     */
    @Override
    protected boolean validateJarFile(File jarfile)
        throws IOException {

        if (triggers == null)
            return (true);
        JarFile jarFile = new JarFile(jarfile);
        for (int i = 0; i < georTriggers.length; i++) {
            Class clazz = null;
            try {
                if (parent != null) {
                    clazz = parent.loadClass(georTriggers[i]);
                } else {
                    clazz = Class.forName(georTriggers[i]);
                }
            } catch (Throwable t) {
                clazz = null;
            }
            if (clazz == null)
                continue;
            String name = georTriggers[i].replace('.', '/') + ".class";
            if (log.isDebugEnabled())
                log.debug(" Checking for " + name);
            JarEntry jarEntry = jarFile.getJarEntry(name);
            if (jarEntry != null) {
                log.info("validateJarFile(" + jarfile +
                    ") - jar not loaded. See Servlet Spec 2.3, "
                    + "section 9.7.2. Offending class: " + name);
                jarFile.close();
                return (false);
            }
        }
        jarFile.close();
        return (true);

    }
}
