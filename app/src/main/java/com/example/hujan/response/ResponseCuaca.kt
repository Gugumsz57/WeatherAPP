package com.example.hujan.response

import com.google.gson.annotations.SerializedName

data class ResponseCuaca(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: Any? = null
)

data class TimesItem(

	@field:SerializedName("datetime")
	val datetime: String? = null,

	@field:SerializedName("h")
	val h: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("value")
	val value: String? = null,

	@field:SerializedName("day")
	val day: String? = null,

	@field:SerializedName("celcius")
	val celcius: String? = null,

	@field:SerializedName("fahrenheit")
	val fahrenheit: String? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("deg")
	val deg: String? = null,

	@field:SerializedName("card")
	val card: String? = null,

	@field:SerializedName("sexa")
	val sexa: String? = null,

	@field:SerializedName("kph")
	val kph: String? = null,

	@field:SerializedName("mph")
	val mph: String? = null,

	@field:SerializedName("ms")
	val ms: String? = null,

	@field:SerializedName("kt")
	val kt: String? = null
)

data class ParamsItem(

	@field:SerializedName("times")
	val times: List<TimesItem?>? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class Data(

	@field:SerializedName("coordinate")
	val coordinate: String? = null,

	@field:SerializedName("level")
	val level: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("domain")
	val domain: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("region")
	val region: String? = null,

	@field:SerializedName("params")
	val params: List<ParamsItem?>? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null,

	@field:SerializedName("tags")
	val tags: String? = null
)
