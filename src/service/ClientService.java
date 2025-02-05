package service;

import model.Client;

import java.util.List;

public interface ClientService {
void create(Client client);
List<Client> readAll();
Client read(int id);
boolean update(Client client,int id);
List<Client> readAllByName(String name);
}
