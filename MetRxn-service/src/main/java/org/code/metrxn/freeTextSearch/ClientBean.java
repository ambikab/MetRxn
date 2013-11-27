package org.code.metrxn.freeTextSearch;


/**
 * Client - created by requesting the embedded Node
 * @author ambika babuji
 *
 */

import java.util.HashMap;

import org.code.metrxn.util.JsonUtil;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ClientBean {

	private Settings immutableSettings;
	
	private Client client;

	//TODO : convert it into a singleton class!!
	public ClientBean() {
		immutableSettings = ImmutableSettings.settingsBuilder().build();
		client = new TransportClient(immutableSettings);
		( (TransportClient) this.client).addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
		//TODO: collect the indices from the class annotations all @Indexed classes
		//TODO: check if an index exists.
		//client.admin().indices().create(new CreateIndexRequest("autocomplete")).actionGet();
	}
	
	public Settings getImmutableSettings() {
		return immutableSettings;
	}

	public void setImmutableSettings(Settings immutableSettings) {
		this.immutableSettings = immutableSettings;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	public static void main(String[] args) {
		HashMap<String, String> source = new HashMap<String, String>();
		source.put("metab", "glucose");
		ClientBean clientBean = new ClientBean();
		clientBean.getClient().prepareIndex("autocomplete","compounds")
		.setRefresh(true)
		.setSource(JsonUtil.toJsonForObject(source).toString())
		.execute().actionGet();
		System.out.println("Data persisted into the index!!");
	}
	
}