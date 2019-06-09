package com.example.avneeshjaiswal.ecommerceserver.Service;

import com.example.avneeshjaiswal.ecommerceserver.Common.Common;
import com.example.avneeshjaiswal.ecommerceserver.Model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by avneesh jaiswal on 15-Mar-18.
 */

public class FirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        updateToServer(refreshedToken);
    }

    private void updateToServer(String refreshedToken) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference tokens = firebaseDatabase.getReference("Tokens");
        Token data = new Token(refreshedToken,true);//false because this token send from client app
        tokens.child(Common.currentUser.getPhone()).setValue(data);
    }
}
