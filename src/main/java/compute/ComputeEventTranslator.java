package compute;
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

import aio.AioClient;
import com.lmax.disruptor.EventTranslatorOneArg;

public class ComputeEventTranslator implements EventTranslatorOneArg<ComputeEvent, AioClient> {

    private static final ComputeEventTranslator INSTANCE = new ComputeEventTranslator();

    private ComputeEventTranslator() {
    }

    public static ComputeEventTranslator getInstance() {
        return INSTANCE;
    }

    @Override
    public void translateTo(ComputeEvent event, long sequence, AioClient client) {
        event.setClient(client);
    }
}
