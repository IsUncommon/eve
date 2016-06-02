package uncmn.eve.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.ryanharter.auto.value.moshi.AutoValueMoshiAdapterFactory;
import com.squareup.moshi.Moshi;
import uncmn.eve.Eve;
import uncmn.eve.converter.moshi.MoshiConverter;
import uncmn.eve.store.sql.SqlStore;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();
  Eve eve;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Moshi moshi = new Moshi.Builder().add(new AutoValueMoshiAdapterFactory()).build();
    MoshiConverter converter = MoshiConverter.create(moshi);
    converter.addConverter(SampleObjectOne.CONVERTER_KEY, SampleObjectOne.class);
    converter.addConverter(SampleObjectTwo.CONVERTER_KEY, SampleObjectTwo.class);
    SqlStore store = SqlStore.create(this, converter);

    eve = Eve.builder(this).store(store).build();

    eve.store().set("hello", "hello");
    eve.store().set("hello", 1);
    int val = eve.store().get("hello");
    Log.d(TAG, "Hello val is - " + val);

    String complexKey1 = "complex1";
    SampleObjectOne obj1 = SampleObjectOne.create(complexKey1, new float[] { 1f, 2.5f });
    eve.store().set(complexKey1, obj1);

    String complexKey2 = "complex2";
    SampleObjectOne obj2 = SampleObjectOne.create(complexKey2, new float[] { 2f, 2.5f });
    eve.store().set(complexKey2, obj2);

    String complexKey3 = "complex3";
    SampleObjectOne obj3 = SampleObjectOne.create(complexKey3, new float[] { 3f, 3.5f });
    eve.store().set(complexKey3, obj3);

    SampleObjectOne retObj3 = eve.store().get(complexKey3);
    Log.d(TAG, "Is obj3 same ? " + obj3.equals(retObj3));

    String sampleKey1 = "sample1";
    SampleObjectTwo sample1 = SampleObjectTwo.create(sampleKey1, new int[] { 1, 2 });
    eve.store().set(sampleKey1, sample1);

    String sampleKey2 = "sample2";
    SampleObjectTwo sample2 = SampleObjectTwo.create(sampleKey2, new int[] { 2, 3 });
    eve.store().set(sampleKey2, sample2);

    String sampleKey3 = "sample3";
    SampleObjectTwo sample3 = SampleObjectTwo.create(sampleKey3, new int[] { 3, 4 });
    eve.store().set(sampleKey3, sample3);

    SampleObjectTwo retSample3 = eve.store().get(sampleKey3);
    Log.d(TAG, "Is sample same ? " + sample3.equals(retSample3));
  }
}
