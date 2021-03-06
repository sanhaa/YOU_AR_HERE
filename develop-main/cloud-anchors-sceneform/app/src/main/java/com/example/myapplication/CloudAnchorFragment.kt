package com.example.myapplication

import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment

class CloudAnchorFragment : ArFragment() {
//    private val messageSnackbarHelper: SnackbarHelper = SnackbarHelper()
//    private val cloudAnchorManager: CloudAnchorManager = CloudAnchorManager()

    override fun getSessionConfiguration(session: Session?): Config {
        planeDiscoveryController.setInstructionView(null)
        val config: Config = super.getSessionConfiguration(session)
        config.cloudAnchorMode = Config.CloudAnchorMode.ENABLED
        return config // return custom configuration
//        return super.getSessionConfiguration(session)
    }
}