/**  
 * 项目名称  ：  bright
 * 文件名称  ：  InMessage.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据实体。
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class InMessage implements Serializable {

	/**
	 * serialVersionUID:TODO
	 */
	private static final long serialVersionUID = -6701892514366019671L;
	private String ToUserName;
	private String FromUserName;
	private String KfAccount;
	private String FromKfAccount;
	private String ToKfAccount;
	private Long CreateTime;
	private String MsgType = "text";
	private Long MsgId;
	// 文本消息
	private String Content;
	// 图片消息
	private String PicUrl;
	// 位置消息
	private String Latitude;
	private String Longitude;
	private Long Scale;
	private String Label;
	// 链接消息
	private String Title;
	private String Description;
	private String Url;
	// 语音信息
	private String MediaId;
	private String Format;
	private String Recognition;
	// 事件
	private String Event;
	private String EventKey;
	private String Ticket;

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public Long getMsgId() {
		return MsgId;
	}

	public void setMsgId(Long msgId) {
		MsgId = msgId;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public Long getScale() {
		return Scale;
	}

	public void setScale(Long scale) {
		Scale = scale;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getEvent() {
		return Event;
	}

	public void setEvent(String event) {
		Event = event;
	}

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}

	public String getRecognition() {
		return Recognition;
	}

	public void setRecognition(String recognition) {
		Recognition = recognition;
	}

	public String getTicket() {
		return Ticket;
	}

	public void setTicket(String ticket) {
		Ticket = ticket;
	}

	public String getKfAccount() {
		return KfAccount;
	}

	public void setKfAccount(String kfAccount) {
		KfAccount = kfAccount;
	}

	public String getFromKfAccount() {
		return FromKfAccount;
	}

	public void setFromKfAccount(String fromKfAccount) {
		FromKfAccount = fromKfAccount;
	}

	public String getToKfAccount() {
		return ToKfAccount;
	}

	public void setToKfAccount(String toKfAccount) {
		ToKfAccount = toKfAccount;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = InMessage.class.getDeclaredFields();
		for (Field field : fields) {
			Object obj;
			try {
				obj = field.get(this);
				if (obj != null) {
					map.put(field.getName(), obj);
				}
				map.remove("ToUserName");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	@Override
	public String toString() {
		return "InMessage [ToUserName=" + ToUserName + ", FromUserName=" + FromUserName + ", KfAccount=" + KfAccount
				+ ", FromKfAccount=" + FromKfAccount + ", ToKfAccount=" + ToKfAccount + ", CreateTime=" + CreateTime
				+ ", MsgType=" + MsgType + ", MsgId=" + MsgId + ", Content=" + Content + ", PicUrl=" + PicUrl
				+ ", Latitude=" + Latitude + ", Longitude=" + Longitude + ", Scale=" + Scale + ", Label=" + Label
				+ ", Title=" + Title + ", Description=" + Description + ", Url=" + Url + ", MediaId=" + MediaId
				+ ", Format=" + Format + ", Recognition=" + Recognition + ", Event=" + Event + ", EventKey=" + EventKey
				+ ", Ticket=" + Ticket + "]";
	}

}
