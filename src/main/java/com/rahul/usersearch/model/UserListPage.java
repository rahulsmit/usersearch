package com.rahul.usersearch.model;

import com.rahul.usersearch.model.user.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListPage {
  List<User> results;
  Info info;
}
