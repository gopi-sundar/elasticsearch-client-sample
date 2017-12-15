package com.arizon;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ProductDO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@JsonProperty( value = "id")
	private Long id;
	@JsonProperty( value = "bc_id")
	private Long bcId;
	@JsonProperty( value = "name")
	private String name;
	@JsonProperty( value = "sku")
	private String sku;
	@JsonProperty( value = "description")
	private String description;
	@JsonProperty( value = "customer_price")
	private Double customerPrice;
	@JsonProperty( value = "retail_price")
	private Double retailPrice;
	@JsonProperty( value = "sale_price")
	private Double salePrice;	
	@JsonProperty( value = "images")
	private List<ProductImageDO> images;
	@JsonProperty( value = "bcUrl")
	private String bcUrl;
	
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getBcId() {
		return bcId;
	}
	public void setBcId(Long bcId) {
		this.bcId = bcId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getCustomerPrice() {
		return customerPrice;
	}
	public void setCustomerPrice(Double customerPrice) {
		this.customerPrice = customerPrice;
	}
	public Double getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}
	public List<ProductImageDO> getImages() {
		return images;
	}
	public void setImages(List<ProductImageDO> images) {
		this.images = images;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bcId == null) ? 0 : bcId.hashCode());
		result = prime * result + ((bcUrl == null) ? 0 : bcUrl.hashCode());
		result = prime * result + ((customerPrice == null) ? 0 : customerPrice.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((images == null) ? 0 : images.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((retailPrice == null) ? 0 : retailPrice.hashCode());
		result = prime * result + ((salePrice == null) ? 0 : salePrice.hashCode());
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
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
		ProductDO other = (ProductDO) obj;
		if (bcId == null) {
			if (other.bcId != null)
				return false;
		} else if (!bcId.equals(other.bcId))
			return false;
		if (bcUrl == null) {
			if (other.bcUrl != null)
				return false;
		} else if (!bcUrl.equals(other.bcUrl))
			return false;
		if (customerPrice == null) {
			if (other.customerPrice != null)
				return false;
		} else if (!customerPrice.equals(other.customerPrice))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (images == null) {
			if (other.images != null)
				return false;
		} else if (!images.equals(other.images))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (retailPrice == null) {
			if (other.retailPrice != null)
				return false;
		} else if (!retailPrice.equals(other.retailPrice))
			return false;
		if (salePrice == null) {
			if (other.salePrice != null)
				return false;
		} else if (!salePrice.equals(other.salePrice))
			return false;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.equals(other.sku))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ProductDO [id=" + id + ", bcId=" + bcId + ", name=" + name + ", sku=" + sku + ", description="
				+ description + ", customerPrice=" + customerPrice + ", retailPrice=" + retailPrice + ", salePrice="
				+ salePrice + ", images=" + images + ", bcUrl=" + bcUrl + "]";
	}
	public ProductDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getBcUrl() {
		return bcUrl;
	}
	public void setBcUrl(String bcUrl) {
		this.bcUrl = bcUrl;
	}
	
	
	
}
