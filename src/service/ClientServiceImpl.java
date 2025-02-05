package service;

import model.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
//Затычка для базы данных клиента
public class ClientServiceImpl implements ClientService {
    private static final Map<Integer,Client> CLIENT_MAP = new HashMap<>();
    private static final AtomicInteger CLIENT_ID_HOLDER = new AtomicInteger();


    @Override
    public void create(Client client) {
    final int clientId = CLIENT_ID_HOLDER.getAndIncrement();
    client.setId(clientId);
    CLIENT_MAP.put(clientId,client);
    }

    @Override
    public List<Client> readAll() {
        return new ArrayList<>(CLIENT_MAP.values());
    }

    @Override
    public Client read(int id) {
        return CLIENT_MAP.get(id);
    }

    @Override
    public boolean update(Client client,int id) {
        if(CLIENT_MAP.containsKey(id)){
            client.setId(id);
            CLIENT_MAP.put(id,client);
            return true;
        }
        return false;
    }

    @Override
    public List<Client> readAllByName(String name) {
        List<Client> list = new ArrayList<>();
        for (Map.Entry<Integer,Client> e : CLIENT_MAP.entrySet()) {
        if(name.trim().equals(e.getValue().getName())){
        list.add(e.getValue());
        }
        }
        return list;
    }
}
