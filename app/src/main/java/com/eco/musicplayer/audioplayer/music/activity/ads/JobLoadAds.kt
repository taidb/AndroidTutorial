package com.eco.musicplayer.audioplayer.music.activity.ads

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobLoadAds {
    private var process =-1
    private var isShowAds =false
    private var delay =20L
    private val couroutineScope by lazy { CoroutineScope(Dispatchers.IO)  }
    private var loopingFlowJob :Job? =null
    private var loopingFlow = flow {
        while (true){
            emit(Unit)
            delay(delay)
        }
    }
    fun setDelay(delay:Long){
        this.delay =delay
    }

    fun startJob(jobProgress:((Int) ->Unit)){
        if (isShowAds()){
            return
        }
        stopJob()
        loopingFlowJob=couroutineScope.launch(Dispatchers.IO) {
            loopingFlow.collect{
                process++
                withContext(Dispatchers.Main){
                    jobProgress.invoke(process)
                }
            }

        }

    }

    fun stopJob(){
        process =-1
        loopingFlowJob?.cancel()
        loopingFlow.cancellable()
    }

    fun isShowAds():Boolean{
        return isShowAds
    }
}