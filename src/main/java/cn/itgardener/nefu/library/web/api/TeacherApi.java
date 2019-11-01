/*
 * Copyright (c) 2014-2019 www.itgardener.cn. All rights reserved.
 */

package cn.itgardener.nefu.library.web.api;

import cn.itgardener.nefu.library.common.ErrorMessage;
import cn.itgardener.nefu.library.common.GlobalConst;
import cn.itgardener.nefu.library.common.LibException;
import cn.itgardener.nefu.library.common.RestData;
import cn.itgardener.nefu.library.common.util.JsonUtil;
import cn.itgardener.nefu.library.common.util.TokenUtil;
import cn.itgardener.nefu.library.common.util.VerifyUtil;
import cn.itgardener.nefu.library.core.model.User;
import cn.itgardener.nefu.library.core.model.vo.*;
import cn.itgardener.nefu.library.service.BookCaseService;
import cn.itgardener.nefu.library.service.ReservationService;
import cn.itgardener.nefu.library.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : pc
 * @date : 2018/10/31
 * @since : Java 8
 */
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("api")
@RestController
public class TeacherApi {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReservationService reservationService;
    private final BookCaseService bookCaseService;
    private final UserService userService;

    @Autowired
    public TeacherApi(ReservationService reservationService, BookCaseService bookCaseService, UserService userService) {
        this.reservationService = reservationService;
        this.bookCaseService = bookCaseService;
        this.userService = userService;
    }

    @RequestMapping(value = "/open-time", method = RequestMethod.GET)
    public RestData getReservationTime() {
        logger.info("GET getReservationTime");

        try {
            final Map<String, String> reservationTime = reservationService.getReservationTime();
            logger.info("get get reservationTime success");
            return new RestData(reservationTime);

        } catch (LibException e) {
            return new RestData(e.getMessage());
        }
    }

    @RequestMapping(value = "/open-time", method = RequestMethod.PUT)
    public RestData putReservationTime(@RequestBody TimeVo timeVO, HttpServletRequest request) {
        logger.info("PUT putReservationTime" + JsonUtil.getJsonString(timeVO));

        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }
        return reservationService.putReservationTime(timeVO);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public RestData getDetail(BookCaseVo bookCaseVo, HttpServletRequest request) {
        logger.info("GET getDetail");

        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }
        return bookCaseService.selectDetailByCondition(bookCaseVo);
    }

    @RequestMapping(value = "/ship", method = RequestMethod.PUT)
    public RestData putShip(@RequestBody ShipVo shipVO, HttpServletRequest request) {
        logger.info("PUT putShip: " + JsonUtil.getJsonString(shipVO));

        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }
        try {
            return new RestData(bookCaseService.putShip(shipVO));
        } catch (LibException e) {
            return new RestData(1, e.getMessage());
        }
    }

    @RequestMapping(value = "/ship", method = RequestMethod.DELETE)
    public RestData deleteShip(@RequestBody List<Integer> data, HttpServletRequest request) {
        logger.info("DELETE deleteShip: " + JsonUtil.getJsonString(data));

        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }
        return bookCaseService.deleteShip(data);
    }

    @RequestMapping(value = "/reserved-id", method = RequestMethod.POST)
    public RestData postKeepById(@RequestBody BookCaseVo bookCaseVo, HttpServletRequest request) {
        logger.info("POST postKeep: " + JsonUtil.getJsonString(bookCaseVo));
        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }
        try {
            VerifyUtil.verifyTime();
        } catch (LibException e) {
            return new RestData(1, e.getMessage());
        }
        return bookCaseService.setKeepById(bookCaseVo);
    }

    @RequestMapping(value = "/reserved-number", method = RequestMethod.POST)
    public RestData postKeepByNumber(@RequestBody BookCaseVo bookCaseVo, HttpServletRequest request) {
        logger.info("POST postKeep: " + JsonUtil.getJsonString(bookCaseVo));

        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }
        try {
            VerifyUtil.verifyTime();
        } catch (LibException e) {
            return new RestData(1, e.getMessage());
        }
        return bookCaseService.setKeepByNumber(bookCaseVo);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RestData getBlackList(HttpServletRequest request) {
        logger.info("GET getBlackList");

        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }
        try {
            List<Map<String, Object>> blackList = userService.getBlackList();
            return new RestData(blackList);
        } catch (LibException e) {
            return new RestData(1, e.getMessage());
        }
    }

    @RequestMapping(value = "/list/{studentId}", method = RequestMethod.DELETE)
    public RestData deleteBlackList(@PathVariable String studentId, HttpServletRequest request) {
        logger.info("DELETE deleteBlackList : studentId = " + studentId);

        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }

        User user = new User();
        user.setStudentId(studentId);

        try {
            return new RestData(userService.deleteBlackListByStudentId(user));
        } catch (LibException e) {
            return new RestData(1, e.getMessage());
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public RestData postLogin(@RequestBody User user, HttpServletRequest request) {
        logger.info("POST postAddBlackApi : " + JsonUtil.getJsonString(user));

        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }
        if (1 == userService.postAddBlackList(user)) {
            return new RestData(true);
        } else {
            return new RestData(1, "添加黑名单失败");
        }
    }

    @RequestMapping(value = "/open-area", method = RequestMethod.PUT)
    public RestData putReservationArea(@RequestBody AreaVo areaVo) {
        logger.info("PUT putReservationArea: " + JsonUtil.getJsonString(areaVo));
        try {
            VerifyUtil.verifyTime();
            return new RestData(reservationService.putReservationArea(areaVo));
        } catch (Exception e) {
            return new RestData(1, e.getMessage());
        }
    }

    @RequestMapping(value = "/bookcase-id", method = RequestMethod.DELETE)
    public RestData deleteBookCaseById(@RequestBody BookCaseVo bookCaseVo) {
        try {
            VerifyUtil.verifyTime();
            return bookCaseService.deleteBookcaseById(bookCaseVo);
        } catch (LibException e) {
            return new RestData(1, e.getMessage());
        }
    }

    @RequestMapping(value = "/bookcase-number", method = RequestMethod.DELETE)
    public RestData deleteBookCaseByNumber(@RequestBody BookCaseVo bookCaseVo) {

        try {
            VerifyUtil.verifyTime();
            return bookCaseService.deleteBookcaseByNumber(bookCaseVo);
        } catch (LibException e) {
            return new RestData(1, e.getMessage());
        }
    }

    @RequestMapping(value = "/student", method = RequestMethod.POST)
    public RestData postStudent(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        logger.info("POST postStudent : ");

        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }

        RestData rtv;
        String fileName = GlobalConst.UPLOAD_PATH + TokenUtil.getToken() + ".xlsx";

        File localFile = new File(fileName);
        try {
            file.transferTo(localFile);
            rtv = userService.postStudent(fileName);
            if (!localFile.delete()) {
                logger.warn("Delete " + fileName + " failed!");
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
            rtv = new RestData(1, e.getLocalizedMessage());
        }

        return rtv;
    }

    @RequestMapping(value = "/student", method = RequestMethod.DELETE)
    public RestData deleteStudent(HttpServletRequest request) {
        logger.info("DELETE deleteStudent : ");

        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }

        userService.deleteStudent();
        return new RestData(true);
    }

    @RequestMapping(value = "/bookcase", method = RequestMethod.POST)
    public RestData AddBookCase(@RequestBody BookCaseVo bookCaseVo, HttpServletRequest request) {
        logger.info("get AddBookCase" + JsonUtil.getJsonString(bookCaseVo));
        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }
        try {
            VerifyUtil.verifyTime();
        } catch (LibException e) {
            return new RestData(1, e.getMessage());
        }
        if (bookCaseService.addBookcase(bookCaseVo)) {
            return new RestData(true);
        } else {
            return new RestData(1, "批量添加柜子失败");
        }
    }

    @RequestMapping(value = "/location", method = RequestMethod.POST)
    public RestData addLocation(@RequestBody LocationVo locationVo, HttpServletRequest request) {
        logger.info("get addLocation:" + JsonUtil.getJsonString(locationVo));
        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }
        try {
            VerifyUtil.verifyTime();
        } catch (LibException e) {
            return new RestData(1, e.getMessage());
        }
        if (bookCaseService.addLocation(locationVo)) {
            Map<String, String> map = new HashMap<>(2);
            map.put("location", locationVo.getLocation());
            return new RestData(map);
        } else {
            return new RestData(1, "添加区域失败");
        }
    }

    @RequestMapping(value = "/location/{location}", method = RequestMethod.DELETE)
    public RestData deleteLocation(@PathVariable String location, HttpServletRequest request) {
        logger.info("deleteLocation:" + JsonUtil.getJsonString(location));
        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }

        try {
            VerifyUtil.verifyTime();
            return bookCaseService.deleteLocation(location);
        } catch (LibException e) {
            return new RestData(1, e.getMessage());
        }

    }

    @RequestMapping(value = "/bookcase-systemId", method = RequestMethod.POST)
    public RestData postBookcase(@RequestBody BookCaseVo bookCaseVo, HttpServletRequest request) {
        logger.info("postBookcase:");
        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }
        try {
            VerifyUtil.verifyTime();
            return bookCaseService.postBookcase(bookCaseVo);
        } catch (LibException e) {
            return new RestData(1, e.getMessage());
        }

    }

    @RequestMapping(value = "/bookcase-systemId", method = RequestMethod.DELETE)
    public RestData deleteBookcase(@RequestBody BookCaseVo bookCaseVo, HttpServletRequest request) {
        logger.info("deleteBookcase:");
        if (!VerifyUtil.verifyType(request)) {
            return new RestData(1, ErrorMessage.OPERATIOND_ENIED);
        }
        try {
            VerifyUtil.verifyTime();
            return bookCaseService.deleteBookcase(bookCaseVo);
        } catch (LibException e) {
            return new RestData(1, e.getMessage());
        }

    }

    @RequestMapping(value = "/downMessage", method = RequestMethod.GET)
    public void downMessage(HttpServletResponse response, HttpServletRequest request) {
        logger.info("downMessage");
        if (!VerifyUtil.verifyType(request)) {
            return;
        }
        bookCaseService.downMessage(response);
    }

}
