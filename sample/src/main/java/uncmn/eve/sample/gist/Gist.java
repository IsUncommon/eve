package uncmn.eve.sample.gist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * An object instance representing Gist.
 */

@AutoValue public abstract class Gist {

  public static final String KEY_PREFIX = "gist";

  public static final String CONVERTER_KEY = Gist.class.getSimpleName();

  public static JsonAdapter<Gist> moshiAdapter(Moshi moshi) {
    return new AutoValue_Gist.MoshiJsonAdapter(moshi);
  }

  @NonNull @Json(name = "url") public abstract String url();

  @NonNull @Json(name = "forks_url") public abstract String forksUrl();

  @NonNull @Json(name = "commits_url") public abstract String commitsUrl();

  @NonNull @Json(name = "id") public abstract String id();

  @NonNull @Json(name = "git_pull_url") public abstract String gitPullUrl();

  @NonNull @Json(name = "git_push_url") public abstract String gitPushUrl();

  @NonNull @Json(name = "html_url") public abstract String htmlUrl();

  @Json(name = "public") public abstract boolean isPublic();

  @NonNull @Json(name = "created_at") public abstract String createdAt();

  @Nullable @Json(name = "updated_at") public abstract String updatedAt();

  @Nullable @Json(name = "description") public abstract String description();

  @Json(name = "comments") public abstract int comments();

  @Nullable @Json(name = "user") public abstract String user();

  @Nullable @Json(name = "comments_url") public abstract String commentsUrl();

  @Json(name = "truncated") public abstract boolean truncated();
}
