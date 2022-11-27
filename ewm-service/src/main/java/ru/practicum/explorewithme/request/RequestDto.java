package ru.practicum.explorewithme.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.events.StatusEvent;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private Long id;
    private Date created;
    private Long event;
    private Long requester;
    private StatusEvent status;
}
