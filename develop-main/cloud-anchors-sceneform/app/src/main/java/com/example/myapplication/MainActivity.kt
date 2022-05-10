package com.example.myapplication

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    lateinit var btn_clear: Button
    lateinit var btn_resolve: Button

    lateinit var msgBtn: ImageButton
    var targetMsg = "this is target message"
    private lateinit var arFragment: CloudAnchorFragment
    enum class AppAnchorState{
        NONE,
        HOSTING,
        HOSTED,
        RESOLVING,
        RESOLVED
    }
    var cloudAnchor: Anchor? = null

    var appAnchorState = AppAnchorState.NONE
    var snackbarHelper: SnackbarHelper =
        SnackbarHelper()
    var storageManager = StorageManager()

//    private val firebaseManager : FirebaseManager? = null

    var modelFile: String = "models/scene.gltf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_clear = findViewById(R.id.btn_clear)
        btn_resolve = findViewById(R.id.btn_resolve)

        arFragment = supportFragmentManager.findFragmentById(R.id.ar_fragment) as CloudAnchorFragment
        arFragment.arSceneView.scene.addOnUpdateListener(this::onUpdateFrame)
        arFragment.planeDiscoveryController.hide()
        arFragment.planeDiscoveryController.setInstructionView(null)

        btn_clear.setOnClickListener{
            cloudAnchor(null)
        }
        btn_resolve.setOnClickListener{
            if(cloudAnchor != null){
                snackbarHelper.showMessage(this, "Please clear the anchor")
                return@setOnClickListener
            }
            val dialog = ResolveDialogFragment()
            dialog.setOkListener(this::onResolveOkPressed)
            dialog.show(supportFragmentManager, "Resolving")
        }

        arFragment.setOnTapArPlaneListener{ hitResult, plane, _ ->
            if(plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING || appAnchorState != AppAnchorState.NONE){
                return@setOnTapArPlaneListener
            }
            val anchor = arFragment.arSceneView.session?.hostCloudAnchor(hitResult.createAnchor())
            cloudAnchor(anchor)
            appAnchorState = AppAnchorState.HOSTING
            snackbarHelper.showMessage(this, "Hosting anchor ><")
            placeObject(arFragment, cloudAnchor!!, Uri.parse(modelFile))
        }
    }

    fun onResolveOkPressed(dialogVal: String) {
        val shortCode = dialogVal.toInt()
        val cloudAnchorId = storageManager.getCloudAnchorID(this, shortCode)
        val resolvedAnchor = arFragment.arSceneView.session?.resolveCloudAnchor(cloudAnchorId)
        cloudAnchor(resolvedAnchor)
        placeObject(arFragment, cloudAnchor!!, Uri.parse("model.sfb"))
        snackbarHelper.showMessage(this, "Now resolving anchor...")
        appAnchorState = AppAnchorState.RESOLVING
    }

    fun onUpdateFrame(frameTime: FrameTime) {
        checkUpdatedAnchor()
    }
    @Synchronized
    private fun checkUpdatedAnchor() {
        if (appAnchorState != AppAnchorState.HOSTING && appAnchorState != AppAnchorState.RESOLVING)
            return
        val cloudState: Anchor.CloudAnchorState = cloudAnchor?.cloudAnchorState!!
        if (appAnchorState == AppAnchorState.HOSTING) {
            if (cloudState.isError) {
                snackbarHelper.showMessage(this, "Error hosting anchor...")
                appAnchorState = AppAnchorState.NONE
            } else if (cloudState == Anchor.CloudAnchorState.SUCCESS) {
                val shortCode = storageManager.nextShortCode(this)
                storageManager.storeUsingShortCode(this, shortCode, cloudAnchor?.cloudAnchorId)
                snackbarHelper.showMessage(this, "Anchor hosted: $shortCode")
                appAnchorState = AppAnchorState.HOSTED
            }
        } else if (appAnchorState == AppAnchorState.RESOLVING) {
            if (cloudState.isError) {
                snackbarHelper.showMessage(this, "Error resolving anchor...")
                appAnchorState = AppAnchorState.NONE
            } else if (cloudState == Anchor.CloudAnchorState.SUCCESS) {
                snackbarHelper.showMessage(this, "Anchor resolved...")
                appAnchorState = AppAnchorState.RESOLVED
            }
        }
    }


    private fun cloudAnchor(newAnchor: Anchor?){
        cloudAnchor?.detach()
        cloudAnchor = newAnchor
        appAnchorState = AppAnchorState.NONE
        snackbarHelper.hide(this)
    }

    private fun placeObject(fragment: ArFragment, anchor: Anchor, model: Uri){
        ModelRenderable.Builder()
            .setSource(fragment.context, model)
            .build()
            .thenAccept{ renderable ->
                addNodeToScene(fragment, anchor, renderable)
            }
            .exceptionally{
                val builder = AlertDialog.Builder(this)
                builder.setMessage(it.message).setTitle("Error!")
                val dialog = builder.create()
                dialog.show()
                return@exceptionally null
            }
    }
    // scene에 node 추가하기
    private fun addNodeToScene(fragment: ArFragment, anchor: Anchor, renderable: ModelRenderable?) {
        val node = AnchorNode(anchor)
        val transformableNode = TransformableNode(fragment.transformationSystem)

        transformableNode.renderable = renderable
        transformableNode.setParent(node)
        fragment.arSceneView.scene.addChild(node)
        transformableNode.select()
    }

//    fun onShortCodeEntered(shortCode: Int) {
//        firebaseManager!!.getCloudAnchorId(shortCode) { cloudAnchorId: String? ->
//            if (cloudAnchorId == null || cloudAnchorId.isEmpty()) {
//                messageSnackbarHelper.showMessage(
//                    this,
//                    "A Cloud Anchor ID for the short code " + shortCode + "was not found T.T"
//                )
//                return@getCloudAnchorId
//            }
//            btn_resolve.setEnabled(false)
//            val resolvedAnchor = arFragment.arSceneView.session?.resolveCloudAnchor(cloudAnchorId)
//            cloudAnchor(resolvedAnchor)
//            placeObject(arFragment, cloudAnchor!!, Uri.parse(modelFile))
//            messageSnackbarHelper.showMessage(this, "Now resolving anchor ^^")
//            appAnchorState = AppAnchorState.RESOLVING
//        }
//    }
}

