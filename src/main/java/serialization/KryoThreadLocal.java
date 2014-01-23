package serialization;
/*
 * Copyright 2014 Yang Fan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

class KryoThreadLocal {

    private static final ThreadLocal<Kryo> LOCAL_KRYO = new ThreadLocal<>();

    private static final ThreadLocal<ByteBufferInput> LOCAL_INPUT = new ThreadLocal<>();

    private static final ThreadLocal<ByteBufferOutput> LOCAL_OUTPUT = new ThreadLocal<>();

    private static final List<Class> CLASSES = new ArrayList<>();

    private static boolean scanned = false;

    public static Kryo getLocalKryo() {
        Kryo k = LOCAL_KRYO.get();
        if (k == null) {
            if (scanned) {
                k = new Kryo();
                registerClasses(k);
                LOCAL_KRYO.set(k);
            } else {
                throw new RuntimeException("Has not specify a Kryo message package, please invoke KryoSerializer.registerKryoClasses(String packageName) at first.");
            }
        }
        return k;
    }

    public static ByteBufferInput getLocalInput() {
        ByteBufferInput byteBufferInput = LOCAL_INPUT.get();
        if (byteBufferInput == null) {
            byteBufferInput = new ByteBufferInput();
            LOCAL_INPUT.set(byteBufferInput);
        }
        return byteBufferInput;
    }

    public static ByteBufferOutput getLocalOutput() {
        ByteBufferOutput byteBufferOutput = LOCAL_OUTPUT.get();
        if (byteBufferOutput == null) {
            byteBufferOutput = new ByteBufferOutput();
            LOCAL_OUTPUT.set(byteBufferOutput);
        }
        return byteBufferOutput;
    }

    private static void registerClasses(Kryo k) {
        for (Class c : CLASSES) {
            k.register(c);
        }
    }

    public static void initRegistration(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        for (File directory : dirs) {
            findClasses(directory, packageName);
        }
        scanned = true;
    }

    private static void findClasses(File directory, String packageName) throws ClassNotFoundException {
        if (!directory.exists()) {
            return;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                findClasses(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                CLASSES.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
    }
}
