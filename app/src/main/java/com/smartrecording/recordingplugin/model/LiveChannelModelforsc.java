package com.smartrecording.recordingplugin.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "LiveChannelModelforsc")

public class LiveChannelModelforsc extends BaseModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long ids;
    @ColumnInfo(name = "uid")
    private long uid;
    @ColumnInfo(name = "connection_id")
    private long connection_id;
    @ColumnInfo(name = "category_id")
    private String category_id;
    @ColumnInfo(name = "category_name")
    private String category_name;
    @ColumnInfo(name = "num")
    private long num;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "stream_type")
    private String stream_type;
    @ColumnInfo(name = "stream_id")
    private String stream_id;
    @ColumnInfo(name = "stream_icon")
    private String stream_icon;
    @ColumnInfo(name = "epg_channel_id")
    private String epg_channel_id;
    @ColumnInfo(name = "user_agent")
    private String user_agent;
    @ColumnInfo(name = "added")
    private String added;
    @ColumnInfo(name = "custom_sid")
    private String custom_sid;
    @ColumnInfo(name = "tv_archive")
    private String tv_archive;
    @ColumnInfo(name = "direct_source")
    private String direct_source;
    @ColumnInfo(name = "tv_archive_duration")
    private String tv_archive_duration;
    @ColumnInfo(name = "parental_control")
    private boolean parental_control = false;
    @ColumnInfo(name = "favourite")
    private boolean favourite = false;
    @ColumnInfo(name = "channel_count_per_group")
    private int channel_count_per_group;
    @ColumnInfo(name = "default_category_index")
    private int default_category_index;
    @ColumnInfo(name = "set_as_default")
    private boolean set_as_default;
    @ColumnInfo(name = "archive")
    private boolean archive = false;
    //merging single epg model
    @ColumnInfo(name = "programme_title")
    private String programme_title;
    @ColumnInfo(name = "programme_desc")
    private String programme_desc;
    @ColumnInfo(name = "start_time")
    private long start_time;
    @ColumnInfo(name = "end_time")
    private long end_time;
    //added for status watched/missed
    @ColumnInfo(name = "channelstatus")
    private String channelstatus;

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }


    public LiveChannelModelforsc() {
    }

    public String getProgramme_title() {
        return programme_title;
    }

    public void setProgramme_title(String programme_title) {
        this.programme_title = programme_title;
    }

    public String getProgramme_desc() {
        return programme_desc;
    }

    public void setProgramme_desc(String programme_desc) {
        this.programme_desc = programme_desc;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public static Creator<LiveChannelModelforsc> getCREATOR() {
        return CREATOR;
    }

    protected LiveChannelModelforsc(Parcel in) {
        uid = in.readLong();
        connection_id = in.readLong();
        category_id = in.readString();
        category_name = in.readString();
        num = in.readLong();
        end_time = in.readLong();
        start_time = in.readLong();
        name = in.readString();
        stream_type = in.readString();
        stream_id = in.readString();
        stream_icon = in.readString();
        epg_channel_id = in.readString();
        user_agent = in.readString();
        added = in.readString();
        custom_sid = in.readString();
        tv_archive = in.readString();
        direct_source = in.readString();
        tv_archive_duration = in.readString();
        programme_desc = in.readString();
        channelstatus = in.readString();
        programme_title = in.readString();
        parental_control = in.readByte() != 0;
        archive = in.readByte() != 0;
        favourite = in.readByte() != 0;
        channel_count_per_group = in.readInt();
        default_category_index = in.readInt();
        set_as_default = in.readByte() != 0;
    }

    public static final Creator<LiveChannelModelforsc> CREATOR = new Creator<LiveChannelModelforsc>() {
        @Override
        public LiveChannelModelforsc createFromParcel(Parcel in) {
            return new LiveChannelModelforsc(in);
        }

        @Override
        public LiveChannelModelforsc[] newArray(int size) {
            return new LiveChannelModelforsc[size];
        }
    };

    public boolean isSet_as_default() {
        return set_as_default;
    }

    public void setSet_as_default(boolean set_as_default) {
        this.set_as_default = set_as_default;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getConnection_id() {
        return connection_id;
    }

    public void setConnection_id(long connection_id) {
        this.connection_id = connection_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public String getStream_type() {
        return stream_type;
    }

    public void setStream_type(String stream_type) {
        this.stream_type = stream_type;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getStream_icon() {
        return stream_icon;
    }

    public void setStream_icon(String stream_icon) {
        this.stream_icon = stream_icon;
    }

    public String getEpg_channel_id() {
        return epg_channel_id;
    }

    public void setEpg_channel_id(String epg_channel_id) {
        this.epg_channel_id = epg_channel_id;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getCustom_sid() {
        return custom_sid;
    }

    public void setCustom_sid(String custom_sid) {
        this.custom_sid = custom_sid;
    }

    public String getTv_archive() {
        return tv_archive;
    }

    public void setTv_archive(String tv_archive) {
        this.tv_archive = tv_archive;
    }

    public String getDirect_source() {
        return direct_source;
    }

    public void setDirect_source(String direct_source) {
        this.direct_source = direct_source;
    }

    public String getTv_archive_duration() {
        return tv_archive_duration;
    }

    public void setTv_archive_duration(String tv_archive_duration) {
        this.tv_archive_duration = tv_archive_duration;
    }

    public boolean isParental_control() {
        return parental_control;
    }

    public void setParental_control(boolean parental_control) {
        this.parental_control = parental_control;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public int getChannel_count_per_group() {
        return channel_count_per_group;
    }

    public void setChannel_count_per_group(int channel_count_per_group) {
        this.channel_count_per_group = channel_count_per_group;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public int getDefault_category_index() {
        return default_category_index;
    }

    public void setDefault_category_index(int default_category_index) {
        this.default_category_index = default_category_index;
    }

    @Override
    public String toString() {
        return "LiveChannelModelforsc{" +
                "ids=" + ids +
                ", uid=" + uid +
                ", connection_id=" + connection_id +
                ", category_id='" + category_id + '\'' +
                ", category_name='" + category_name + '\'' +
                ", num=" + num +
                ", name='" + name + '\'' +
                ", stream_type='" + stream_type + '\'' +
                ", stream_id='" + stream_id + '\'' +
                ", stream_icon='" + stream_icon + '\'' +
                ", epg_channel_id='" + epg_channel_id + '\'' +
                ", user_agent='" + user_agent + '\'' +
                ", added='" + added + '\'' +
                ", custom_sid='" + custom_sid + '\'' +
                ", tv_archive='" + tv_archive + '\'' +
                ", direct_source='" + direct_source + '\'' +
                ", tv_archive_duration='" + tv_archive_duration + '\'' +
                ", parental_control=" + parental_control +
                ", favourite=" + favourite +
                ", channel_count_per_group=" + channel_count_per_group +
                ", default_category_index=" + default_category_index +
                ", set_as_default=" + set_as_default +
                ", archive=" + archive +
                ", programme_title='" + programme_title + '\'' +
                ", programme_desc='" + programme_desc + '\'' +
                ", channelstatus='" + channelstatus + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(uid);
        parcel.writeLong(connection_id);
        parcel.writeString(category_id);
        parcel.writeString(category_name);
        parcel.writeLong(num);
        parcel.writeLong(end_time);
        parcel.writeLong(start_time);
        parcel.writeString(name);
        parcel.writeString(stream_type);
        parcel.writeString(stream_id);
        parcel.writeString(stream_icon);
        parcel.writeString(epg_channel_id);
        parcel.writeString(user_agent);
        parcel.writeString(added);
        parcel.writeString(custom_sid);
        parcel.writeString(tv_archive);
        parcel.writeString(direct_source);
        parcel.writeString(tv_archive_duration);
        parcel.writeString(programme_desc);
        parcel.writeString(channelstatus);
        parcel.writeString(programme_title);
        parcel.writeByte((byte) (parental_control ? 1 : 0));
        parcel.writeByte((byte) (archive ? 1 : 0));
        parcel.writeByte((byte) (favourite ? 1 : 0));
        parcel.writeInt(channel_count_per_group);
        parcel.writeInt(default_category_index);
        parcel.writeByte((byte) (set_as_default ? 1 : 0));
    }

    public long getIds() {
        return ids;
    }

    public void setIds(long ids) {
        this.ids = ids;
    }

    public String getChannelstatus() {
        return channelstatus;
    }

    public void setChannelstatus(String channelstatus) {
        this.channelstatus = channelstatus;
    }
}
