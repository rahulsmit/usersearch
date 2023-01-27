package com.rahul.usersearch.model;

import com.rahul.usersearch.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.cluster.metadata.AliasAction;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressListPage {
  List<Address> results;
}
