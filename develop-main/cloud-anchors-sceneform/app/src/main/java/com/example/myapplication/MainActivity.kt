package com.example.myapplication

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.core.codelab.cloudanchor.helpers.CloudAnchorManager
import com.google.ar.core.codelab.cloudanchor.helpers.CloudAnchorManager.CloudAnchorListener
import com.google.ar.core.codelab.cloudanchor.helpers.FirebaseManager
import com.google.ar.core.codelab.cloudanchor.helpers.ResolveDialogFragment
import com.google.ar.core.codelab.cloudanchor.helpers.SnackbarHelper
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
    private var appAnchorState: AppAnchorState = AppAnchorState.NONE

    private val messageSnackbarHelper: SnackbarHelper = SnackbarHelper()
    private val cloudAnchorManager = CloudAnchorManager()
    private val firebaseManager : FirebaseManager? = null

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
                messageSnackbarHelper.showMessage(this, "Please clear the anchor")
                return@setOnClickListener
            }
            val dialog = ResolveDialogFragment.createWithOkListener{this::onShortCodeEntered }
            dialog.show(supportFragmentManager, "Resolving")
        }

        arFragment.setOnTapArPlaneListener{ hitResult, plane, _ ->
            if(plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING || appAnchorState != AppAnchorState.NONE){
                return@setOnTapArPlaneListener
            }
            val anchor = arFragment.arSceneView.session?.hostCloudAnchor(hitResult.createAnchor())
            cloudAnchor(anchor)
            appAnchorState = AppAnchorState.HOSTING
            messageSnackbarHelper.showMessage(this, "Hosting anchor ><")
            placeObject(arFragment, cloudAnchor!!, Uri.parse(modelFile))
        }

        /* TODO: support Fragment Manager: arfragment와 inputfragment 관리 */
//        var f: Fragment? = supportFragmentManager.findFragmentById(R.id.arFragment)
//        if(arFragment is f)
//            arFragment = f

//        supportFragmentManager.beginTransaction().replace(R.id.containerFragment, MainFragment()).commit()

//        msgBtn = findViewById<ImageButton>(R.id.addButton)
//        msgBtn.setOnClickListener{
////            val nextIntent = Intent(this, InputFragment::class.java)
////            startActivity(nextIntent)
//            supportFragmentManager.beginTransaction().replace(R.id.inputFragment, InputFragment()).addToBackStack(null).commit()
//        }
//        if (intent.hasExtra("message")){
//            val txt = intent.getStringExtra("message")
////            renderableTxt.setText(txt)
//            Toast.makeText(this, "전달 받음: "+txt, Toast.LENGTH_SHORT).show()
//        }
//        supportFragmentManager.commit {
//            add(R.id.containerFragment, MainFragment::class.java, Bundle())
//        }

    }

    // anchor가 성공적으로 cloud에 호스팅 되었는지 카메라 프레임마다 체크함
    fun onUpdateFrame(frameTime: FrameTime) {
        checkUpdateAnchor()
    }
    @Synchronized
    private fun checkUpdateAnchor() {
        // short code 이용해서 cloud anchor id 저장, 불러옴

        if(appAnchorState != AppAnchorState.HOSTING && appAnchorState != AppAnchorState.RESOLVING)
            return

        val cloudState: Anchor.CloudAnchorState = cloudAnchor?.cloudAnchorState!!
        // HOSTING
        if (appAnchorState == AppAnchorState.HOSTING) {
            if (cloudState.isError){
                messageSnackbarHelper.showMessage(this, "Error hosting anchor T.T")
                appAnchorState = AppAnchorState.NONE
            } else if (cloudState == Anchor.CloudAnchorState.SUCCESS){
                // anchor host 처리: FirebaseManager를 이용하여 anchor ID  ->shortcode 변환후 저장
                val cloudAnchorId: String? = cloudAnchor?.cloudAnchorId
                firebaseManager!!.nextShortCode { shortCode: Int? ->
                    if (shortCode != null) {
                        firebaseManager!!.storeUsingShortCode(shortCode, cloudAnchorId)
                        messageSnackbarHelper.showMessage(this, "Cloud Anchor Hosted on firebase. Short code: $shortCode")
                        appAnchorState = AppAnchorState.HOSTED
                    } else {
                        // Firebase could not provide a short code
                        messageSnackbarHelper.showMessage(this, "Cloud Anchor Hosted, but FAIL to get short code from firebase ")
                    }
                }
            }
        }
        ///// RESOLVING
        else if (appAnchorState == AppAnchorState.RESOLVING){
            if(cloudState.isError){
                messageSnackbarHelper.showMessage(this, "Error resolving anchor T.T")
                appAnchorState = AppAnchorState.NONE
            } else if (cloudState == Anchor.CloudAnchorState.SUCCESS){
                messageSnackbarHelper.showMessage(this, "Anchor Resolved !!!")
                appAnchorState = AppAnchorState.RESOLVED
            }
        }
    }

    private fun cloudAnchor(newAnchor: Anchor?){
        cloudAnchor?.detach()
        cloudAnchor = newAnchor
        appAnchorState = AppAnchorState.NONE
        messageSnackbarHelper.hide(this)
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

    fun onShortCodeEntered(shortCode: Int) {
        firebaseManager!!.getCloudAnchorId(shortCode) { cloudAnchorId: String? ->
            if (cloudAnchorId == null || cloudAnchorId.isEmpty()) {
                messageSnackbarHelper.showMessage(
                    this,
                    "A Cloud Anchor ID for the short code " + shortCode + "was not found T.T"
                )
                return@getCloudAnchorId
            }
            btn_resolve.setEnabled(false)
            val resolvedAnchor = arFragment.arSceneView.session?.resolveCloudAnchor(cloudAnchorId)
            cloudAnchor(resolvedAnchor)
            placeObject(arFragment, cloudAnchor!!, Uri.parse(modelFile))
            messageSnackbarHelper.showMessage(this, "Now resolving anchor ^^")
            appAnchorState = AppAnchorState.RESOLVING
        }
    }
}

