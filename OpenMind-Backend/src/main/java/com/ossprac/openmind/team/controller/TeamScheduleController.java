package com.ossprac.openmind.team.controller;

import com.ossprac.openmind.team.dto.req.TeamScheduleAddRequest;
import com.ossprac.openmind.team.dto.req.TeamScheduleDeleteRequest;
import com.ossprac.openmind.team.dto.res.PersonalScheduleResponse;
import com.ossprac.openmind.team.dto.res.TeamScheduleResponse;
import com.ossprac.openmind.team.service.TeamScheduleService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamScheduleController {
    private final TeamScheduleService teamScheduleService;
    @ApiOperation("개인 시간표 가져오는 API")
    @GetMapping("/{teamId}/personal-schedule")
    public ResponseEntity<PersonalScheduleResponse> getMembers(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamScheduleService.getPersonalSchedule(teamId));
    }

    @ApiOperation("팀 시간표 가져오는 API")
    @GetMapping("/{teamId}/schedule")
    public ResponseEntity<List<TeamScheduleResponse>> getTeamSchedule(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamScheduleService.getTeamSchedule(teamId));
    }

    @ApiOperation("팀 시간표 추가 API")
    @PostMapping("/schedule")
    public ResponseEntity<String> addTeamSchedule(@RequestBody TeamScheduleAddRequest teamScheduleAddRequest) {
        teamScheduleService.addSchedule(teamScheduleAddRequest);
        return ResponseEntity.ok("success");
    }

    @ApiOperation("시간표 삭제 API")
    @DeleteMapping("/schedule")
    public ResponseEntity<String> deleteTeamSchedule(@RequestBody TeamScheduleDeleteRequest teamScheduleDeleteRequest) {
        teamScheduleService.deleteSchedule(teamScheduleDeleteRequest);
        return ResponseEntity.ok("success");
    }

}
