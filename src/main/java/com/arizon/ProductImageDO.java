package com.arizon;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ProductImageDO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty( value = "id")
	private Integer id;
	@JsonProperty( value = "is_thumbnail")
	private Boolean isThumbnail;
	@JsonProperty( value = "thumbnail_url")
	private String thumbnailUrl;
	@JsonProperty( value = "standard_url")
	private String standardUrl;
	@JsonProperty( value = "sortOrder")
	private Integer sortOrder;
	@JsonProperty( value = "alt_text")
	private String altText;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getIsThumbnail() {
		return isThumbnail;
	}
	public void setIsThumbnail(Boolean isThumbnail) {
		this.isThumbnail = isThumbnail;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public String getStandardUrl() {
		return standardUrl;
	}
	public void setStandardUrl(String standardUrl) {
		this.standardUrl = standardUrl;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getAltText() {
		return altText;
	}
	public void setAltText(String altText) {
		this.altText = altText;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((altText == null) ? 0 : altText.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isThumbnail == null) ? 0 : isThumbnail.hashCode());
		result = prime * result + ((sortOrder == null) ? 0 : sortOrder.hashCode());
		result = prime * result + ((standardUrl == null) ? 0 : standardUrl.hashCode());
		result = prime * result + ((thumbnailUrl == null) ? 0 : thumbnailUrl.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductImageDO other = (ProductImageDO) obj;
		if (altText == null) {
			if (other.altText != null)
				return false;
		} else if (!altText.equals(other.altText))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isThumbnail == null) {
			if (other.isThumbnail != null)
				return false;
		} else if (!isThumbnail.equals(other.isThumbnail))
			return false;
		if (sortOrder == null) {
			if (other.sortOrder != null)
				return false;
		} else if (!sortOrder.equals(other.sortOrder))
			return false;
		if (standardUrl == null) {
			if (other.standardUrl != null)
				return false;
		} else if (!standardUrl.equals(other.standardUrl))
			return false;
		if (thumbnailUrl == null) {
			if (other.thumbnailUrl != null)
				return false;
		} else if (!thumbnailUrl.equals(other.thumbnailUrl))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ProductImageDO [id=" + id + ", isThumbnail=" + isThumbnail + ", thumbnailUrl=" + thumbnailUrl
				+ ", standardUrl=" + standardUrl + ", sortOrder=" + sortOrder + ", altText=" + altText + "]";
	}
	
	
	
	
	

}