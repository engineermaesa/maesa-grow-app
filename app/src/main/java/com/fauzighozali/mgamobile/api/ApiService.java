package com.fauzighozali.mgamobile.api;

import com.fauzighozali.mgamobile.model.GetResponseBook;
import com.fauzighozali.mgamobile.model.GetResponseCalendar;
import com.fauzighozali.mgamobile.model.GetResponseCourse;
import com.fauzighozali.mgamobile.model.GetResponseDetailCourse;
import com.fauzighozali.mgamobile.model.GetResponseDetailUser;
import com.fauzighozali.mgamobile.model.GetResponseInbox;
import com.fauzighozali.mgamobile.model.GetResponseLeaderboard;
import com.fauzighozali.mgamobile.model.GetResponseMessage;
import com.fauzighozali.mgamobile.model.GetResponseToken;
import com.fauzighozali.mgamobile.model.GetResponseVideo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("login_mobile")
    @FormUrlEncoded
    Call<GetResponseToken> login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("isWeb") int isWeb,
            @Field("token") String token
    );

    @POST("refresh")
    @FormUrlEncoded
    Call<GetResponseToken> refresh(
            @Field("refresh_token") String refreshToken
    );

    @POST("mobile/logout")
    @FormUrlEncoded
    Call<GetResponseToken> logout(
            @Field("token") String token
    );

    @POST("mobile/change_password")
    @FormUrlEncoded
    Call<GetResponseMessage> changePassword(
            @Field("old_password") String old_password,
            @Field("new_password") String new_password,
            @Field("confirm_password") String confirm_password
    );

    @GET("mobile/detail_user")
    Call<GetResponseDetailUser> getDetailUser();

    @GET("mobile/books")
    Call<GetResponseBook> getBook();

    @GET("mobile/vhs")
    Call<GetResponseVideo> getVideo();

    @GET("mobile/course_list_dashboard")
    Call<GetResponseCourse> getCourseCertificationDashboard(
            @Query("type") int type,
            @Query("is_mobile") int is_mobile
    );

    @GET("mobile/leaderboards")
    Call<GetResponseLeaderboard> getLeaderboard();

    @GET("mobile/user_course")
    Call<GetResponseCourse> getHistoryCourse();

    @GET("web/event")
    Call<GetResponseInbox> getInbox(
            @Query("company_id") int company_id
    );

    @POST("mobile/accept_course")
    @FormUrlEncoded
    Call<GetResponseMessage> acceptCourse(
            @Field("course_id") int course_id
    );

    @GET("mobile/course_detail/{id}")
    Call<GetResponseDetailCourse> getCourseDetail(
            @Path("id") int id
    );

    @POST("mobile/submit_question")
    @FormUrlEncoded
    Call<GetResponseMessage> sumbitQuestion(
            @Field("course_id") int course_id,
            @Field("score") int score
    );
}
