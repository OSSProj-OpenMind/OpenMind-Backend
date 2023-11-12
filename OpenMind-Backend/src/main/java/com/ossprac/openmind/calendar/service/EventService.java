package com.ossprac.openmind.calendar.service;

import com.ossprac.openmind.calendar.dto.EventRequestDto;
import com.ossprac.openmind.calendar.dto.EventResponseDto;
import com.ossprac.openmind.calendar.dto.EventUpdateRequestDto;
import com.ossprac.openmind.calendar.entity.Event;
import com.ossprac.openmind.calendar.repository.EventRepository;
import com.ossprac.openmind.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    @Autowired
    private final EventRepository eventRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public EventResponseDto addEvent(EventRequestDto eventRequestDto) {
        Event event = eventRequestDto.toEntity();
        if (event.getStartDate().isAfter(event.getEndDate())) {
            throw new IllegalArgumentException("startDate 가 endDate 보다 늦습니다.");
        }
        Event savedEvent = eventRepository.save(event);
        teamRepository.findById(eventRequestDto.getTeamId()).ifPresent(savedEvent::setTeam);
        return getResponseEventDto(savedEvent);
    }

    @Transactional(readOnly = true)
    public EventResponseDto getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        return getResponseEventDto(event);
    }

    // team id 로 event 리스트 조회
    @Transactional(readOnly = true)
    public List<EventResponseDto> getEventListByTeamId(Long teamId) {
        List<Event> eventList = eventRepository.findAllByTeamId(teamId);
        List<EventResponseDto> eventResponseDtoList = eventList.stream()
                .map(this::getResponseEventDto)
                .collect(Collectors.toList());
        return eventResponseDtoList;
    }

    private EventResponseDto getResponseEventDto(Event savedEvent) {
        return EventResponseDto.builder()
                .id(savedEvent.getId())
                .title(savedEvent.getTitle())
                .description(savedEvent.getDescription())
                .startDate(savedEvent.getStartDate())
                .endDate(savedEvent.getEndDate())
                .build();
    }

    public EventResponseDto updateEvent(Long eventId, EventUpdateRequestDto requestDto) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        event.update(requestDto);
        Event savedEvent = eventRepository.save(event);
        return getResponseEventDto(savedEvent);
    }
}