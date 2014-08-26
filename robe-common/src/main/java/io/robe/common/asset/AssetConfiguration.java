package io.robe.common.asset;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class AssetConfiguration {

	@Valid
	@NotNull
	@JsonProperty
	private String resourcePath;

	@Valid
	@NotNull
	@JsonProperty
	private String uriPath;

	@Valid
	@NotNull
	@JsonProperty
	private String indexFile;

	@Valid
	@NotNull
	@JsonProperty
	private String assetsName;

	public String getResourcePath() {
		return resourcePath;
	}

	public String getUriPath() {
		return uriPath;
	}

	public String getIndexFile() {
		return indexFile;
	}

	public String getAssetsName() {
		return assetsName;
	}
}
