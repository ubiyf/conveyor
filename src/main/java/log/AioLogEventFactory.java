package log;

import com.lmax.disruptor.EventFactory;

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
public class AioLogEventFactory implements EventFactory<AioLogEvent> {

    private static final AioLogEventFactory INSTANCE = new AioLogEventFactory();

    private AioLogEventFactory() {}

    public static AioLogEventFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public AioLogEvent newInstance() {
        return new AioLogEvent();
    }
}
