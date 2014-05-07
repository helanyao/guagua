/**
 * Copyright [2013-2014] eBay Software Foundation
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ml.shifu.guagua.master;

import java.util.concurrent.TimeUnit;

import ml.shifu.guagua.io.Bytable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link MasterTimer} is used to get execution time of the post intercepters and master computation. This time includes
 * waiting time and should be set in system intercepters.
 * 
 * @param <MASTER_RESULT>
 *            master computation result in each iteration.
 * @param <WORKER_RESULT>
 *            worker computation result in each iteration.
 */
public class MasterTimer<MASTER_RESULT extends Bytable, WORKER_RESULT extends Bytable> implements
        MasterInterceptor<MASTER_RESULT, WORKER_RESULT> {

    private static final Logger LOG = LoggerFactory.getLogger(MasterTimer.class);

    private long applicationStartTime;

    private long iterationStartTime;

    @Override
    public void preApplication(MasterContext<MASTER_RESULT, WORKER_RESULT> context) {
        this.applicationStartTime = System.nanoTime();
        LOG.info("Application {} container {} starts master computation.", context.getAppId(), context.getContainerId());
    }

    @Override
    public void preIteration(MasterContext<MASTER_RESULT, WORKER_RESULT> context) {
        this.iterationStartTime = System.nanoTime();
        LOG.info("Application {} container {} iteration {} starts master computation.", context.getAppId(),
                context.getContainerId(), context.getCurrentIteration());
    }

    @Override
    public void postIteration(MasterContext<MASTER_RESULT, WORKER_RESULT> context) {
        LOG.info("Application {} container {} iteration {} ends with {}ms execution time.", context.getAppId(),
                context.getContainerId(), context.getCurrentIteration(),
                TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - this.iterationStartTime));
    }

    @Override
    public void postApplication(MasterContext<MASTER_RESULT, WORKER_RESULT> context) {
        LOG.info("Application {} container {} ends with {}ms execution time.", context.getAppId(),
                context.getContainerId(), TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - this.applicationStartTime));
    }

}