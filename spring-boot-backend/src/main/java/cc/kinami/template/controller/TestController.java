package cc.kinami.template.controller;


import cc.kinami.template.model.dto.*;
import cc.kinami.template.model.po.DayMemo;
import cc.kinami.template.model.po.Memo;
import cc.kinami.template.service.TestService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Api(value = "测试用接口")
@RestController
@AllArgsConstructor
//@RequestMapping("/api/test")
@RequestMapping("/api")
public class TestController {

    TestService testService;

//    @ApiOperation(value = "helloworld", notes = "this is an example")
//    @RequestMapping(value = "/hello", method = RequestMethod.GET)
//    public ResponseDTO<String> helloWorld(@RequestParam(required = false) @ApiParam("233") String name) {
//        if (name == null)
//            return new ResponseDTO<>(200, "OK", "Hello, world!");
//        else
//            return new ResponseDTO<>(200, "OK", "Hello, " + name + "!");
//    }
//
//    @ApiOperation(value = "exception test", notes = "this is an example")
//    @RequestMapping(value = "/error", method = RequestMethod.GET)
//    public ResponseDTO<Object> testError() {
//        throw new KnownException(ErrorInfoEnum.RHQ_HUNGRY);
//    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseDTO<String> register(@RequestBody UserPostDTO userPostDTO) {
        return new ResponseDTO<>(200, "OK", testService.registerCheck(userPostDTO.getUsername(), userPostDTO.getPassword()));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseDTO<String> login(@RequestBody UserPostDTO userPostDTO) {
        return new ResponseDTO<>(200, "OK", testService.loginCheck(userPostDTO.getUsername(), userPostDTO.getPassword()));
    }

    //    @RequestMapping(value = "/insertUser", method = RequestMethod.POST)
//    public ResponseDTO<String> testInsertUser(
//            @RequestBody Map<String, List<Integer>> param
//    ) {
//        System.out.println(param);
//        List<Integer> userList = param.get("userList");
//        testService.insertUsers(userList);
//        return new ResponseDTO<>(200, "ok", "");
//
//    }
    @RequestMapping(value = "/newMemo", method = RequestMethod.POST)
    public ResponseDTO<String> newMemo(@RequestBody NewMemoPostDTO newMemoPostDTO) {
        return new ResponseDTO<>(200, "OK",
                testService.newMemo(newMemoPostDTO.getUsername(), newMemoPostDTO.getDeadline(),
                        newMemoPostDTO.getHeadline(), newMemoPostDTO.getDetail()));
    }

    @RequestMapping(value = "/editMemo", method = RequestMethod.POST)
    public ResponseDTO<String> editMemo(@RequestBody MemoPostDTO memoPostDTO) {
        return new ResponseDTO<>(200, "OK", testService.editMemo(memoPostDTO.getId(), memoPostDTO.getDeadline(), memoPostDTO.getHeadline(), memoPostDTO.getDetail()));
    }

    @RequestMapping(value = "/deleteMemo", method = RequestMethod.POST)
    public ResponseDTO<String> deleteMemo(@RequestBody DeleteMemoPostDTO deleteMemoPostDTO) {
        return new ResponseDTO<>(200, "OK", testService.deleteMemo(deleteMemoPostDTO.getId()));
    }

    @RequestMapping(value = "/allMemos", method = RequestMethod.GET)
    public ResponseDTO<List<Memo>> allMemos(@RequestParam String username) {
        return new ResponseDTO<>(200, "OK", testService.selectAllMemos(username));
    }

    @RequestMapping(value = "/dayMemos", method = RequestMethod.GET)
    public ResponseDTO<List<DayMemo>> dayMemos(@RequestParam String username,
                                               @RequestParam String deadline) throws ParseException {
        return new ResponseDTO<>(200, "OK",
                testService.selectDayMemos(username, deadline));
    }
//    @RequestMapping(value = "/select", method = RequestMethod.GET)
//    public ResponseDTO<List<User>> testSelect() {
//        return new ResponseDTO<>(200, "OK", testService.selectAllUsers());
//    }

//    @RequestMapping(value = "/select", method = RequestMethod.GET)
//    public ResponseDTO<List<User>> testSelect() {
//        return new ResponseDTO<>(200, "OK", testService.selectAllUsers());
//    }
//
//    @RequestMapping(value = "/select2", method = RequestMethod.GET)
//    public ResponseDTO<List<User>> testSelect2(
//            @RequestParam int test
//    ) {
//        return new ResponseDTO<>(200, "OK", testService.selectUserByTest(test));
//    }


}
