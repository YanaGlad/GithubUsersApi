package com.example.testgithubusersexample;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ProgressBar;

        import java.util.List;

public class MainActivity extends AppCompatActivity implements MainPresenter.Listener {

    RecyclerView recyclerView;
    private MainPresenter presenter;
    private Button retryButton;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(view -> {
            presenter.loadUsers();
            retryButton.setVisibility(View.GONE);
            loadingBar.setVisibility(View.VISIBLE);
        });
        loadingBar = findViewById(R.id.loadingBar);

        recyclerView = findViewById(R.id.users_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        GitHubService service = ((GitHubApplication)getApplication()).getGitHubService();
        presenter = new MainPresenter(service, this);
        presenter.loadUsers();
        loadingBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onUsersLoaded(List<User> users) {
        loadingBar.setVisibility(View.GONE);
        recyclerView.setAdapter(new UserAdapter(users));
    }

    @Override
    public void onError(String message) {
        loadingBar.setVisibility(View.GONE);
        retryButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {//прилож закрывается
        super.onDestroy();
        presenter.stopLoading();
    }
}