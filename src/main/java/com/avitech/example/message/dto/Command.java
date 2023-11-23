package com.avitech.example.message.dto;

import com.avitech.example.enums.DbCommand;
import com.avitech.example.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@ToString
public class Command {

    DbCommand dbCommand;

    User userToAdd;
}
