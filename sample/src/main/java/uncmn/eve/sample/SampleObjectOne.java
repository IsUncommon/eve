package uncmn.eve.sample;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * Complex object resource.
 */
@AutoValue public abstract class SampleObjectOne {
  public static final String CONVERTER_KEY = "SampleObjectOne";

  public abstract String name();

  public abstract float[] items();

  public static SampleObjectOne create(String name, float[] items) {
    return new AutoValue_SampleObjectOne(name, items);
  }

  public static JsonAdapter<SampleObjectOne> jsonAdapter(Moshi moshi) {
    return new AutoValue_SampleObjectOne.MoshiJsonAdapter(moshi);
  }
}
