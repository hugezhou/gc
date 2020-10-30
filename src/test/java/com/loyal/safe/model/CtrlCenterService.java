package com.loyal.safe.model;

import com.loyal.safe.framework.config.MyConfig;
import com.loyal.safe.framework.datatables.DataTablesRequest;
import com.loyal.safe.framework.redis.UserCacheRedis;
import com.loyal.safe.framework.response.ServiceResponse;
import com.loyal.safe.framework.service.BaseService;
import com.loyal.safe.framework.service.MyService;
import com.loyal.safe.framework.service.SysRegionService;
import com.loyal.safe.framework.service.SysUserService;
import com.loyal.safe.framework.utils.ListMapUtils;
import com.loyal.safe.framework.utils.RequestUtils;
import com.loyal.safe.model.patrolMore.PatrolMoreRecord;
import com.loyal.safe.webService.HiddenDangerControlServiceWeb;
import com.loyal.safe.webService.PatrolMoreRecordServiceWeb;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CtrlCenterService extends MyService<CtrlCenter> {

    @Autowired
    private CtrlCenterMapper ctrlCenterMapper;

    @Autowired
    private SysRegionService sysRegionService;

    @Autowired
    private GatewayRepairMapper gatewayRepairMapper;

    @Autowired
    private UserCacheRedis userCacheRedis;

    @Autowired
    private CtrlCenterTreeService ctrlCenterTreeService;

    @Autowired
    private PatrolMoreRecordServiceWeb patrolMoreRecordServiceWeb;

    @Autowired
    private HiddenDangerControlServiceWeb hiddenDangerControlServiceWeb;

    @Autowired
    private HiddenDangerControlMapper hiddenDangerControlMapper;

    @Autowired
    private UnitService unitService;

    @Autowired
    private UnitDtlService unitDtlService;

    @Autowired
    private SelfPatrolMapper selfPatrolMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private PoliceMapper policeMapper;

    @Autowired
    private MyConfig myConfig;

    private final static String ROOT_ID = "d07a9d0e78e047668b2dc66a67f8c273";


    public List<CtrlCenter> findCtrlCenterList(DataTablesRequest dataTablesRequest) {
        String regionId = dataTablesRequest.getColumnValue("regionId");
        List<String> regionIdList = new ArrayList<>();
        if (StringUtils.isNotEmpty(regionId)) {
            regionIdList = sysRegionService.findRegionByPid(regionId);
        }
        String name = dataTablesRequest.getColumnValue("name");
        String parentName = dataTablesRequest.getColumnValue("parentName");
        CtrlCenter ctrlCenter = new CtrlCenter();
        if (CollectionUtils.isNotEmpty(regionIdList)) {
            ctrlCenter.setRegionIdList(regionIdList);
        }
        ctrlCenter.setSelCtrlIdList(userCacheRedis.getSelCtrlIdList());
        ctrlCenter.setParentName(parentName);
        ctrlCenter.setName(name);
        ctrlCenter.setOrdersby(dataTablesRequest.orders());
        RequestUtils.startPage(dataTablesRequest);
        return findCtrlCenterList(ctrlCenter);
    }

    public List<CtrlCenter> findCtrlCenterList(CtrlCenter ctrlCenter) {
        List<CtrlCenter> ctrlCenterList = ctrlCenterMapper.findCtrlCenterList(ctrlCenter);
        return ctrlCenterList;
    }

    public List<CtrlCenter> findCtrlCenterListByParentId(String parentId) {
        CtrlCenter ctrlCenter = new CtrlCenter();
        ctrlCenter.setParentId(parentId);
        return ctrlCenterMapper.findCtrlCenterList(ctrlCenter);
    }

    public ServiceResponse deleteById(String id) {
        List<CtrlCenter> ctrlCenterList = findCtrlCenterListByParentId(id);
        if (CollectionUtils.isNotEmpty(ctrlCenterList)) {
            return ServiceResponse.error("删除失败,请先删除子监控中心");
        }
        CtrlCenter ctrlCenter = selectByKey(id);

        if (expire(ctrlCenter) != BaseService.SUCCESS) {
            return ServiceResponse.error("删除失败");
        }
        opLog(ctrlCenter, OP_DEL);
        return ServiceResponse.succ("删除成功");
    }

    public CtrlCenter getCtrlCenter(String id) {
        CtrlCenter ctrlCenter = new CtrlCenter();
        ctrlCenter.setId(id);
        List<CtrlCenter> ctrlCenterList = ctrlCenterMapper.findCtrlCenterList(ctrlCenter);
        if (CollectionUtils.isEmpty(ctrlCenterList)) {
            return new CtrlCenter();
        }
        return ctrlCenterList.get(0);
    }


    public List<CtrlCenter> findLinkCtrlCenter(CtrlCenter ctrlCenter){
        return ctrlCenterMapper.findCtrlCenterByLS(ctrlCenter);

    }


    /**
     * 单位id和监控中心名称的对应关系
     */
    public Map<String, String> findUnit2CtrlCenter() {
        List<Map<String, Object>> map = ctrlCenterMapper.findUnit2CtrlCenter();
        Map<String, Object> temp = ListMapUtils.ListMap2Map(map);
        Map<String, String> result = ListMapUtils.MapObject2String(temp);
        return result;
    }

    public ServiceResponse update(CtrlCenter ctrlCenter) {
        // 校验父节点是否合理
        if (ctrlCenter.getId().equals(ctrlCenter.getParentId())) {
            return ServiceResponse.error("不能选择自身节点作为父节点");
        }
        List<String> idList = ctrlCenterTreeService.findCtrlCenterByPid(ctrlCenter.getId());
        if (CollectionUtils.isNotEmpty(idList)) {
            for (int i = 0; i < idList.size(); i++) {
                String cur = idList.get(i);
                if (cur.equals(ctrlCenter.getParentId())) {
                    return ServiceResponse.error("不能选择自身的子节点作为父节点");
                }
            }
        }
        if (updateNotNull(ctrlCenter) != BaseService.SUCCESS) {
            return ServiceResponse.error("更新失败");
        }
        opLog(ctrlCenter, OP_MOD);
        return ServiceResponse.succ("更新成功");
    }

    //拿到所有的上级监控中心id
    public List<String> getParentId(String id) {
        List<String> ids = new ArrayList<>();
        ids.add(id);
        CtrlCenter ctrlCenterid = new CtrlCenter();
        ctrlCenterid.setId(id);
        CtrlCenter ctrlCenter = ctrlCenterMapper.getParentId(ctrlCenterid);
        String parentId = ctrlCenter.getParentId();

        ids.add(parentId);
        int i = 0;

        ctrlCenterid.setId(parentId);

        while (true){

            log.info("parentId-----"+parentId);





            ctrlCenter = ctrlCenterMapper.getParentId(ctrlCenterid);
            if (null != ctrlCenter)
                parentId = ctrlCenter.getParentId();
            ctrlCenterid.setId(parentId);

            ids.add(parentId);

            log.info("ctrlCenter-----"+ctrlCenter);


            if (ctrlCenter.getName().equals("浙江中辰集团监控中心"))
                break;




        }

        return ids;
    }



    public PatrolSta getPatrolSta(String id){

        PatrolSta patrolSta = new PatrolSta();

        if (ROOT_ID.equals(id)){



            //总巡更数
            List<PatrolMoreRecord> patrolMoreRecords = patrolMoreRecordServiceWeb.totalByDate(new PatrolMoreRecord());
            patrolSta.setTotalX(patrolMoreRecords.size());

            //上报隐患
            HiddenDangerControl hiddenDangerControl = new HiddenDangerControl();
            List<HiddenDangerControl> hiddenDangerControls = hiddenDangerControlServiceWeb.totalByAll(hiddenDangerControl);
            patrolSta.setTotalS(hiddenDangerControls.size());

            hiddenDangerControl.setStatus(HiddenDangerControl.STATUS_FOUR);
            hiddenDangerControl.setAdopt(false);
            List<HiddenDangerControl> hiddenDangerControlss = hiddenDangerControlServiceWeb.totalByAll(hiddenDangerControl);
            patrolSta.setTotalZ(hiddenDangerControlss.size());

            return patrolSta;


        }

        List<String> ctrlCenterIdList = ctrlCenterTreeService.findCtrlCenterByPid(id);
        List<String> list = unitService.findUnitIdListByCtrlCenterIds(ctrlCenterIdList);


        //总巡更数
        PatrolMoreRecord patrolMoreRecord = new PatrolMoreRecord();
        patrolMoreRecord.setUnitIdList(list);
        List<PatrolMoreRecord> patrolMoreRecords = patrolMoreRecordServiceWeb.totalByDate(patrolMoreRecord);
        patrolSta.setTotalX(patrolMoreRecords.size());

        //上报隐患
        HiddenDangerControl hiddenDangerControl = new HiddenDangerControl();
        hiddenDangerControl.setSelCtrlIdList(ctrlCenterIdList);
        hiddenDangerControl.setSelUnitIdList(list);
        List<HiddenDangerControl> hiddenDangerControls = hiddenDangerControlServiceWeb.totalByAll(hiddenDangerControl);
        patrolSta.setTotalS(hiddenDangerControls.size());

        hiddenDangerControl.setStatus(HiddenDangerControl.STATUS_FOUR);
        hiddenDangerControl.setAdopt(false);
        List<HiddenDangerControl> hiddenDangerControlss = hiddenDangerControlServiceWeb.totalByAll(hiddenDangerControl);
        patrolSta.setTotalZ(hiddenDangerControlss.size());

        return patrolSta;

    }


    public Map<String,String> listByEmge(String id){

        if (ROOT_ID.equals(id)){

            Integer units = unitService.getCount(new Unit());


            Map<String,String> map = new HashMap<>();

            map.put("count", String.valueOf(units));

            return map;

        }


        List<String> ctrlCenterIdList = ctrlCenterTreeService.findCtrlCenterByPid(id);

        Unit unit = new Unit();

        unit.setCtrlCenterIdList(ctrlCenterIdList);

        Integer units = unitService.getCount(unit);

        if(null == units)
            units = 0;

        Map<String,String> map = new HashMap<>();

        map.put("count", String.valueOf(units));

        return map;

    }

    public List<Map<String,String>> findSiteRank(String ctrlCenterId) {

        List<String> list = ctrlCenterTreeService.findCtrlCenterByPid(ctrlCenterId);
        list.add(ctrlCenterId);

        List<Map<String,String>> maps = new ArrayList<>();


        List<HiddenDangerControl> numberHid = hiddenDangerControlMapper.groupByCtrlCenterIdId(new HiddenDangerControl());
        List<SelfPatrol> numberSelf= selfPatrolMapper.groupByCtrlCenterIdId(new SelfPatrol());
        List<Police> numberPo  = policeMapper.groupByCtrlCenterIdIdToP(new Police());
        List<Police> numberToIn= policeMapper.groupByCtrlCenterIdIdToIn(new Police());
        List<GatewayRepair> numberGate= gatewayRepairMapper.groupByCtrlCenterIdId(new GatewayRepair());


        for (int i = 0; i < list.size(); i ++){

            //算出每个人任务总数总数
            String id = list.get(i);

            Integer num = 0;
            Integer hdcNum = 0;
            Integer spNum = 0;
            Integer poNum = 0;
            Integer oInum = 0;
            Integer grNum = 0;


            for (int j = 0 ; j < numberHid.size(); j ++){
                HiddenDangerControl hdc = numberHid.get(j);
                if (hdc.getCtrlCenterId().equals(id)){
                    hdcNum = hdc.getNum();
                    num = num + hdcNum;
                }
            }

            for (int j = 0 ; j < numberSelf.size(); j ++){
                SelfPatrol sp = numberSelf.get(j);
                String unitId = sp.getUnitId();
                Unit unit = unitService.getUnit(unitId);
                if (null != unit
                        && null != unit.getCtrlCenterId()
                        && !"".equals(unit.getCtrlCenterId())
                        && unit.getCtrlCenterId().equals(id)){
                    spNum = sp.getNum();
                    num = num + spNum;
                }
            }

            for (int j = 0 ; j < numberPo.size(); j ++){
                Police police = numberPo.get(j);
                if (police.getCtrlCenterId().equals(id)){
                    poNum = police.getNum();
                    num = num + poNum;
                }
            }

            for (int j = 0 ; j < numberToIn.size(); j ++){
                Police police = numberToIn.get(j);
                if (police.getCtrlCenterId().equals(id)){
                    oInum = police.getNum();
                    num = num + oInum;
                }
            }

            for (int j = 0 ; j < numberGate.size(); j ++){
                GatewayRepair gr = numberGate.get(j);

                String unitId = gr.getUnitId();
                Unit unit = unitService.getUnit(unitId);
                if (null != unit
                        && null != unit.getCtrlCenterId()
                        && !"".equals(unit.getCtrlCenterId())
                        && unit.getCtrlCenterId().equals(id)){
                    grNum = gr.getNum();
                    num = num + grNum;
                }
            }

            if (num != 0){
                Map<String,String> map = new HashMap<>();
                map.put("id",id);
                map.put("num", String.valueOf(num));
                map.put("ctrlcenterName",getCtrlCenter(id).getName());



                maps.add(map);
            }


        }

        if (oderBy(maps).size() < 10)
            return oderBy(maps);
        else
            return oderBy(maps).subList(0,10);
    }


    public List<Map<String,String>> findSitePersonnelRank(String ctrlCenterId) {

        List<String> list = ctrlCenterTreeService.findCtrlCenterByPid(ctrlCenterId);
        list.add(ctrlCenterId);

        SysUser sysUser = new SysUser();
        sysUser.setType(1);
        sysUser.setSelCtrlIdList(list);
        sysUser.setRoleId(myConfig.getCtrlCenterRoleId());

        List<SysUser> sysUsers = sysUserService.findSysUserList(sysUser);

        List<Map<String,String>> maps = new ArrayList<>();


        List<HiddenDangerControl> numberHid = hiddenDangerControlMapper.groupByIssuedId(new HiddenDangerControl());
        List<SelfPatrol> numberSelf= selfPatrolMapper.groupByIssuedId(new SelfPatrol());
        List<Police> numberPo  = policeMapper.groupByIssuedIdToP(new Police());
        List<Police> numberToIn= policeMapper.groupByIssuedIdToIn(new Police());
        List<GatewayRepair> numberGate= gatewayRepairMapper.groupByIssuedId(new GatewayRepair());


        for (int i = 0; i < sysUsers.size(); i ++){

            //算出每个人任务总数总数
            SysUser sysUserGroup = sysUsers.get(i);
            String id = sysUserGroup.getId();

            Integer num = 0;
            Integer hdcNum = 0;
            Integer spNum = 0;
            Integer poNum = 0;
            Integer oInum = 0;
            Integer grNum = 0;


            for (int j = 0 ; j < numberHid.size(); j ++){
                HiddenDangerControl hdc = numberHid.get(j);
                if (hdc.getIssuedId().equals(id)){
                    hdcNum = hdc.getNum();
                    num = num + hdcNum;
                }
            }

            for (int j = 0 ; j < numberSelf.size(); j ++){
                SelfPatrol sp = numberSelf.get(j);
                if (sp.getCheckUserId().equals(id)){
                    spNum = sp.getNum();
                    num = num + spNum;
                }
            }

            for (int j = 0 ; j < numberPo.size(); j ++){
                Police police = numberPo.get(j);
                if (police.getIssuedId().equals(id)){
                    poNum = police.getNum();
                    num = num + poNum;
                }
            }

            for (int j = 0 ; j < numberToIn.size(); j ++){
                Police police = numberToIn.get(j);
                if (police.getIssuedId().equals(id)){
                    oInum = police.getNum();
                    num = num + oInum;
                }
            }

            for (int j = 0 ; j < numberGate.size(); j ++){
                GatewayRepair gr = numberGate.get(j);
                if (gr.getIssuedId().equals(id)){
                    grNum = gr.getNum();
                    num = num + grNum;
                }
            }

            if (num != 0){
                Map<String,String> map = new HashMap<>();
                map.put("id",sysUserGroup.getId());
                map.put("num", String.valueOf(num));
                map.put("hdcNum", String.valueOf(hdcNum));
                map.put("spNum", String.valueOf(spNum));
                map.put("poNum", String.valueOf(poNum));
                map.put("oInum", String.valueOf(oInum));
                map.put("grNum", String.valueOf(grNum));
                map.put("name", sysUserGroup.getLoginName());
                map.put("ctrlcenterName",getCtrlCenter(sysUserGroup.getUnitId()).getName());



                maps.add(map);
            }


        }

        if (oderBy(maps).size() < 5)
            return oderBy(maps);
        else
            return oderBy(maps).subList(0,5);
    }

    public List<Map<String,String>> oderBy(List<Map<String,String>> maps){
        for (int i = 0; i < maps.size() - 1; i++){

            int minPos = i;

            for (int j = i + 1; j < maps.size();j++){

                minPos = compateTo(maps.get(j),maps.get(minPos)) == -1 ? j : minPos;
            }

            swap(maps,i,minPos);

        }

        return maps;
    }

    void swap(List<Map<String,String>> arr, int i , int j){
        Map<String,String> temp = arr.get(i);
        arr.set(i,arr.get(j));
        arr.set(j,temp);
    }

    public int compateTo(Map<String,String> o1, Map<String,String> o2) {
        Integer i1 = Integer.valueOf(o1.get("num"));
        Integer i2 = Integer.valueOf(o2.get("num"));
        if (i1 > i2) return -1;
        else if (i1 < i2) return 1;
        else return 0;
    }

    public UnitDtl listByTrain(String ctrlCenterId){


        if (ROOT_ID.equals(ctrlCenterId)) {

            UnitDtl unitDtl = unitDtlService.getCount(new UnitDtl());

            if (null != unitDtl){
                unitDtl.setInTimes(unitDtl.getInternalTrainingTimes());

                unitDtl.setNumberUnits(unitDtl.getNumberOfTrainingUnits());

                unitDtl.setNumberTrainees(unitDtl.getNumberOfTrainees());

                unitDtl.setTimesTraining(unitDtl.getTimesOfExternalTraining());
            }
            if (null == unitDtl){
                unitDtl = new UnitDtl();

                unitDtl.setInTimes(0);

                unitDtl.setNumberUnits(0);

                unitDtl.setNumberTrainees(0);

                unitDtl.setTimesTraining(0);
            }



            return unitDtl;
        }

        List<String> ctrlCenterIdList = ctrlCenterTreeService.findCtrlCenterByPid(ctrlCenterId);

        Unit unit = new Unit();

        unit.setCtrlCenterIdList(ctrlCenterIdList);


        List<Unit> units = unitService.findUnitList(unit);


        List<String> unitList = new ArrayList<>();

        for (int i = 0 ; i < units.size(); i++){
            String id = units.get(i).getId();

            unitList.add(id);
        }

        UnitDtl unitDtl = new UnitDtl();

        unitDtl.setUnitIdList(unitList);



        UnitDtl unitDtls = unitDtlService.getCount(unitDtl);

        if (null != unitDtls){


            unitDtls.setInTimes(unitDtls.getInternalTrainingTimes());

            unitDtls.setNumberUnits(unitDtls.getNumberOfTrainingUnits());

            unitDtls.setNumberTrainees(unitDtls.getNumberOfTrainees());

            unitDtls.setTimesTraining(unitDtls.getTimesOfExternalTraining());

        }
        if (null == unitDtls){

            unitDtls = new UnitDtl();
            unitDtls.setInTimes(0);

            unitDtls.setNumberUnits(0);

            unitDtls.setNumberTrainees(0);

            unitDtls.setTimesTraining(0);
        }



        return unitDtls;

    }

    /**
     * 下发任务统计
     * @param ctrlCenterId
     * @return
     */
    public List<CtrlCenter> getTaskStatus(String ctrlCenterId){
        List<String> list = new ArrayList<>();


        if (!ROOT_ID.equals(ctrlCenterId)){
            list = ctrlCenterTreeService.findCtrlCenterByPid(ctrlCenterId);
            list.add(ctrlCenterId);
        }



        // 1 隐患
        Integer a = 0;//完成
        Integer b = 0;//进行
        Integer c = 0;//未完成

        HiddenDangerControl hiddenDangerControl = new HiddenDangerControl();
        hiddenDangerControl.setSelCtrlIdList(list);
        List<HiddenDangerControl> hiddenDangerControls = hiddenDangerControlMapper.groupByStatus(hiddenDangerControl);
        for (int i = 0 ; i < hiddenDangerControls.size(); i++){
            hiddenDangerControl = hiddenDangerControls.get(i);
            Integer count = hiddenDangerControl.getNum();
            Integer status =hiddenDangerControl.getStatus();
            if (status == 4){
                a = count;
            }else if(status == 2 ||
                    status == 3 ||
                    status == 5){
                b = b + count;
            }else if (status == 1 ||
                    status == 6){
                c = c + count;
            }
        }


        //2 巡查
        Integer a2 = 0;//完成
        Integer b2 = 0;//进行
        Integer c2 = 0;//未完成

        SelfPatrol selfPatrol = new SelfPatrol();
        selfPatrol.setSelCtrlIdList(list);
        List<SelfPatrol> selfPatrols = selfPatrolMapper.getCountBS(selfPatrol);
        for (int i = 0 ; i < selfPatrols.size(); i++){
            selfPatrol = selfPatrols.get(i);
            Integer total = selfPatrol.getTotal();
            Byte status = selfPatrol.getStatus();
            if (status == 10){
                a2 = total;
            }else if (status == 1){
                b2 = total;
            }else if (status == 0){
                c2 = total;
            }
        }

        //3 出警
        Integer a3 = 0;//完成
        Integer b3 = 0;//进行
        Integer c3 = 0;//未完成

        Police police = new Police();
        police.setSelCtrlIdList(list);
        List<Police> policeList = policeMapper.groupByStatusOne(police);
        for (int i = 0; i < policeList.size(); i++){
            police = policeList.get(i);
            String status = police.getStatus();
            Integer num = police.getNum();
            if (status.equals("1")){
                a3 = num;
            }else if (status.equals("0")){
                b3 = num;
            }else if (status.equals("2")){
                c3 = num;
            }
        }

        //4 检查
        Integer a4 = 0;//完成
        Integer b4 = 0;//进行
        Integer c4 = 0;//未完成

        policeList = null;
        policeList = policeMapper.groupByStatusTwo(police);
        for (int i = 0; i < policeList.size(); i++){
            police = policeList.get(i);
            String status = police.getStatus();
            Integer num = police.getNum();
            if (status.equals("1")){
                a4 = num;
            }else if (status.equals("0")){
                b4 = num;
            }else if (status.equals("2")){
                c4 = num;
            }
        }

        //5 维修
        Integer a5 = 0;//完成
        Integer b5 = 0;//进行
        Integer c5 = 0;//未完成

        GatewayRepair gatewayRepair = new GatewayRepair();
        gatewayRepair.setSelCtrlIdList(list);
        List<GatewayRepair> gatewayRepairs
                = gatewayRepairMapper.groupByStatus(gatewayRepair);
        for (int i = 0 ; i < gatewayRepairs.size(); i++){
            gatewayRepair = gatewayRepairs.get(i);
            Integer repairOrderStatus = gatewayRepair.getRepairOrderStatus();
            Integer num = gatewayRepair.getNum();
            if (repairOrderStatus == 4){
                a5 = num;
            }else if (repairOrderStatus == 3){
                b5 = num;
            }else if (repairOrderStatus == 2){
                c5 = num;
            }

        }

        double finsh = a + a2 + a3 +a4 + a5;
        double doing = b + b2 + b3+ b4 + b5;
        double fail  = c + c2 + c3+ c4 + c5;

        double all = finsh + doing + fail;

        List<CtrlCenter> ctrlCenters = new ArrayList<>();

        ctrlCenters.add(putCtrlCenter("已完成",finsh,finsh/all));
        ctrlCenters.add(putCtrlCenter("进行中",doing,doing/all));
        ctrlCenters.add(putCtrlCenter("未完成",fail,fail/all));


        return ctrlCenters;

    }

    public CtrlCenter putCtrlCenter(String statisticsName,
                                    Double statisticsNum,
                                    Double statisticsPro){

        CtrlCenter center = new CtrlCenter();
        center.setStatisticsName(statisticsName);
        center.setStatisticsNum(statisticsNum);
        center.setStatisticsPro(statisticsPro);
        return center;
    }


}
