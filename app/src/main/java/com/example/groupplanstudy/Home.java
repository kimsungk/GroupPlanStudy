package com.example.groupplanstudy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.groupplanstudy.DB.UserDB;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.groupplanstudy.databinding.ActivityHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity {

    public APIMessage apiMessage;

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        apiMessage = (APIMessage)intent.getSerializableExtra("user");

        Log.d("전달받은값 : ", apiMessage.getData().toString());

        User user = new User();
        String str = apiMessage.getData().toString();

        try {
            JSONObject jsonObject = new JSONObject(str);

            user.setUid(jsonObject.getLong("uid"));
            user.setEmail(jsonObject.getString("email"));
            user.setPassword(jsonObject.getString("password"));
            user.setIntroduce(jsonObject.getString("introduce"));
            user.setNickname(jsonObject.getString("nickname"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        long uid = user.getUid();
        String email = user.getEmail();
        String password = user.getPassword();
        String introduce = user.getIntroduce();
        String nickname = user.getNickname();

        UserDB userDB = new UserDB(this);

        userDB.userInsert(uid, email, password, introduce, nickname);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_opengroup,
                R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

}