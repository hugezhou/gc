package com.loyal.safe.service;

import com.loyal.safe.framework.redis.ListRedis;
import com.loyal.safe.framework.redis.MapRedis;
import com.loyal.safe.framework.service.SysUserService;
import com.loyal.safe.framework.validator.SystemConstant;
import com.loyal.safe.model.SysUser;
import com.loyal.safe.model.Unit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UnitRedisService {

    @Autowired
    private MapRedis mapRedis;

    @Autowired
    private UnitService unitService;

    @Autowired
    private ListRedis listRedis;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CtrlCenterService ctrlCenterService;

    @Autowired
    private CtrlCenterTreeService ctrlCenterTreeService;

    private static Map<String, String> unitName = new HashMap<>();

    public void refresh() {
        refreshId();
        refreshUnit();
        refreshId2CtrlCenterName();
        refreshUnitTree();
    }

    private void refreshId() {
        Map<String, String> id2Code = new HashMap<>();
        Map<String, Integer> id2ReceptionTime = new HashMap<>();
        Map<String, String> id2Name = new HashMap<>();
        List<Unit> unitList = unitService.findSimpleUnitList(new Unit());
        unitList.forEach(unit -> {
            id2Name.put(unit.getId(), unit.getName());
            id2ReceptionTime.put(unit.getId(), unit.getReceptionTime());
            id2Code.put(unit.getId(), unit.getCode());
        });
        mapRedis.put("Unit::Id2Name", id2Name);
        mapRedis.put("Unit::Id2Code", id2Code);
        mapRedis.put("Unit::Id2ReceptionTime", id2ReceptionTime);
    }

    private void refreshUnit() {
        List<Unit> unitList = unitService.findSimpleUnitList(new Unit());
        listRedis.put("Unit::Simple", unitList);
    }

    public List<Unit> getSimple() {
        List<Unit> unitList = listRedis.get("Unit::Simple");
        if (CollectionUtils.isEmpty(unitList)) {
            refreshUnit();
            return listRedis.get("Unit::Simple");
        }
        return unitList;
    }

    /**
     * 单位树
     */
    private void refreshUnitTree() {
        List<Unit> unitList = getSimple();
        List<Unit> rootList = new ArrayList<>();
        // 生成根节点
        for (int i = 0; i < unitList.size(); i++) {
            if (StringUtils.isEmpty(unitList.get(i).getParentId()) || "0".equals(unitList.get(i).getParentId())) {
                rootList.add(unitList.get(i));
                unitList.remove(i--);
            }
        }
        for (int i = 0; i < rootList.size(); i++) {
            list2Tree(unitList, rootList.get(i));
        }
        listRedis.put("Unit::UnitTree", rootList);
    }

    public List<Unit> getUnitTree() {
        List<Unit> unitList = listRedis.get("Unit::UnitTree");
        if (CollectionUtils.isEmpty(unitList)) {
            refreshUnitTree();
            return listRedis.get("Unit::UnitTree");
        }
        return unitList;
    }

    public Unit getUnitTreeByUnitId(String unitId) {
        return getUnitTree4TreeList(unitId, getUnitTree());
    }

    public Unit getUnitTree4TreeList(String unitId, List<Unit> unitList) {
        for (int i = 0; i < unitList.size(); i++) {
            Unit unit = unitList.get(i);
            if (unit.getId().equals(unitId)) {
                return unit;
            }
            if (CollectionUtils.isEmpty(unit.getUnitList())) {
                continue;
            }
            Unit result = getUnitTree4TreeList(unitId, unit.getUnitList());
            if (StringUtils.isEmpty(result.getId())) {
                continue;
            }
            return result;
        }
        return new Unit();
    }

    private void list2Tree(List<Unit> unitList, Unit unit) {
        List<Unit> _ctrlCenters = new ArrayList<>();
        for (int index = 0; index < unitList.size(); index++) {
            Unit _ctrlCenter = unitList.get(index);// 数据容器:sysRegionList
            // 子集
            if (_ctrlCenter.getParentId() != null
                    && _ctrlCenter.getParentId().equals(unit.getId())
                    && !_ctrlCenter.getId().equals(unit.getId())) {
                list2Tree(unitList, _ctrlCenter);
                /** 递归[子树-_sysRegion] */
                _ctrlCenters.add(_ctrlCenter);
            }
        }
        if (_ctrlCenters.size() > 0) {
            unit.setUnitList(_ctrlCenters);
            unit.setParent(true);
        }
    }



    public Map<String, String> getId2Name() {
        unitName = mapRedis.get("Unit::Id2Name");
        if (MapUtils.isEmpty(unitName)) {
            refreshId();
            return mapRedis.get("Unit::Id2Name");
        }
        return unitName;
    }


    public String getUnitName(String key){
        if(unitName.containsKey(key)){
            return unitName.get(key);
        }else {
            getId2Name();
            return unitName.get(key);
        }
    }


    public Map<String, String> getId2Code() {
        Map<String, String> id2Code = mapRedis.get("Unit::Id2Code");
        if (MapUtils.isEmpty(id2Code)) {
            refreshId();
            return mapRedis.get("Unit::Id2Code");
        }
        return id2Code;
    }

    public Map<String, Integer> getId2ReceptionTime() {
        Map<String, Integer> id2ReceptionTime = mapRedis.get("Unit::Id2ReceptionTime");
        if (MapUtils.isEmpty(id2ReceptionTime)) {
            refreshId();
            return mapRedis.get("Unit::Id2ReceptionTime");
        }
        return id2ReceptionTime;
    }

    private Map<String, String> refreshId2CtrlCenterName() {
        Map<String, String> id2Name2 = ctrlCenterService.findUnit2CtrlCenter();
        mapRedis.put("Unit::Id2CtrlCenterName", id2Name2);
        return id2Name2;
    }

    /**
     * 单位id和监控中心名称的对应关系
     */
    public Map<String, String> getId2CtrlCenterName() {
        Map<String, String> id2Name = mapRedis.get("Unit::Id2CtrlCenterName");
        if (MapUtils.isEmpty(id2Name)) {
            return refreshId2CtrlCenterName();
        }
        return id2Name;
    }


    /**
     * 用户权限内的单位id
     */
    public List<String> getSubUnitId() {
        SysUser sysUser = sysUserService.getSysUser();
        return getSubUnitId(sysUser.getId());
    }

    /**
     * 子单位id
     */
    public List<String> getSelUnitIdListBySelCtrlId(String selCtrlId) {
        if(ctrlCenterTreeService.isRootId(selCtrlId)){
             return new ArrayList<>();
        }
        List<String> ids = ctrlCenterTreeService.findCtrlCenterByPid(selCtrlId);
        List<String> rootUnitIdList = unitService.findUnitIdListByCtrlCenterIds(ids);
        List<String> unitIdList = getSubUnitIdByUnitId(rootUnitIdList);
        return unitIdList;
    }

    /**
     * 子单位id
     */
    public List<String> getSelUnitIdListBySelUnitId(String selUnitId) {
        List<String> rootUnitIdList = new ArrayList<>();
        rootUnitIdList.add(selUnitId);
        List<String> unitIdList = getSubUnitIdByUnitId(rootUnitIdList);
        return unitIdList;
    }

    /**
     * 管辖单位列表
     */
    public List<String> getSubUnitId(String userId) {
        SysUser sysUser = sysUserService.getSysUserById(userId);
        return getSubUnitId(sysUser);
    }

    /**
     * 管辖单位列表
     * @param sysUser
     * @return
     */
    public List<String> getSubUnitId(SysUser sysUser){
        List<String> rootUnitIdList = new ArrayList<>(); // 第一级单位
        if ( SystemConstant.USER_TYPE_CTRL.equals(sysUser.getType()) || SystemConstant.USER_TYPE_GRID.equals(sysUser.getType())) {
            // 监控中心列表
            if (ctrlCenterTreeService.isRootId(sysUser.getUnitId())) { // 特殊优化, 如果是根节点,则返回空列表
                return rootUnitIdList;
            }
            List<String> ids = ctrlCenterTreeService.findCtrlCenterByPid(sysUser.getUnitId());
            rootUnitIdList = unitService.findUnitIdListByCtrlCenterIds(ids);
        } else if (SystemConstant.USER_TYPE_UNIT.equals(sysUser.getType())) {
            rootUnitIdList.add(sysUser.getUnitId());
        }
        List<String> unitIdList = getSubUnitIdByUnitId(rootUnitIdList);
        return unitIdList;
    }




    private List<String> getSubUnitIdByUnitId(List<String> rootUnitIdList) {
        List<String> strList = new ArrayList<>();
        List<Unit> unitList = getUnitTree();
        for (int j = 0; j < rootUnitIdList.size(); j++) {
            String cur = rootUnitIdList.get(j);
            for (int i = 0; i < unitList.size(); i++) {
                Unit unit = unitList.get(i);
                if (unit.getId().equals(cur)) {
                    getUnitId4Unit(unit, strList);
                }
            }
            if(!StringUtils.isEmpty(cur)){
                strList.add(cur);
            }
        }
        return strList;
    }

    private void getUnitId4Unit(Unit unit, List<String> unitIdList) {
        unitIdList.add(unit.getId());
        List<Unit> unitList = unit.getUnitList();
        if (CollectionUtils.isEmpty(unitList)) {
            return;
        }
        for (int i = 0; i < unitList.size(); i++) {
            getUnitId4Unit(unitList.get(i), unitIdList);
        }
    }
}
