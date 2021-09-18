package com.example.tripplanner;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;


public class AR extends AppCompatActivity
{

    private ArFragment arFragment;
    private ModelRenderable modelRenderable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ar_activity);
        maybeEnableArButton();
    }

    private void setUpModel() {
        ModelRenderable.builder()
                .setSource(this, R.raw.wolves)
                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast.makeText(AR.this,"Model can't be Loaded", Toast.LENGTH_SHORT).show();
                    return null;
                });
//        ModelRenderable.builder()
//                .setSource(this, Uri.parse("wolves.sfb"))
//                .build()
//                .thenAccept(renderable -> modelRenderable = renderable)
//                .exceptionally(throwable -> {
//                    Toast.makeText(AR.this,"Model can't be Loaded", Toast.LENGTH_SHORT).show();
//                    return null;
//                });
    }
    private void setUpPlane(){
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            createModel(anchorNode);
        });
    }

    private void createModel(AnchorNode anchorNode){
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(modelRenderable);
        node.select();

    }
    void maybeEnableArButton() {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        if (availability.isTransient()) {
            // Continue to query availability at 5Hz while compatibility is checked in the background.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    maybeEnableArButton();
                }
            }, 200);
        }
        if (availability.isSupported()) {
            arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
            setUpModel();
            setUpPlane();
        } else { // The device is unsupported or unknown.
            Toast toast = Toast.makeText(this,"your Device Cant Handle AR Feature", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
    }

}
