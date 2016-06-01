package uncmn.eve.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.squareup.moshi.Moshi;
import uncmn.eve.Eve;
import uncmn.eve.converter.moshi.MoshiConverter;
import uncmn.eve.store.sql.SqlStore;

public class MainActivity extends AppCompatActivity {

  Eve eve;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Moshi moshi = new Moshi.Builder().build();

    SqlStore store = SqlStore.create(this, MoshiConverter.create(moshi));

    eve = Eve.builder(this).store(store).build();

    eve.store().set("hello", "hello");
    eve.store().set("hello", 1);
    eve.store().print("hello");
  }
}
