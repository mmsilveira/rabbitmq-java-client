package br.com.msilveira.mq.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class OrderMQ implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String phone;
	private String address;
	private String itemDescription;
	private TypeItem typeItem;

	public OrderMQ(String name, String phone, String address, String itemDescription, TypeItem typeItem) {
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.itemDescription = itemDescription;
		this.typeItem = typeItem;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public TypeItem getTypeItem() {
		return typeItem;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append(this.getName())
			.append(this.getPhone())
			.append(this.getAddress())
			.append(this.getItemDescription())
			.append(this.getTypeItem().toString()).toString();
	}

}
