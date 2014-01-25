package hash;

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
public class LuaHash {

    public static long hash(int i) {
        long result = 0;
        byte [] tmp = ByteUtils.intToBytes(i);

        return result;
    }

    public static void main(String [] arg) {
        System.out.println(ByteUtils.intToBytes(1));
        System.out.println(ByteUtils.intToBytes(127));
        System.out.println(ByteUtils.intToBytes(128));
        System.out.println(ByteUtils.intToBytes(130));
    }
}