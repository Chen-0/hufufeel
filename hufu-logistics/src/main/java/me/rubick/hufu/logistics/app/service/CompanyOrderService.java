package me.rubick.hufu.logistics.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import me.rubick.hufu.logistics.app.exception.BaseException;
import me.rubick.hufu.logistics.app.library.Common;
import me.rubick.hufu.logistics.app.model.Company;
import me.rubick.hufu.logistics.app.model.CompanyExpress;
import me.rubick.hufu.logistics.app.model.CompanyOrder;
import me.rubick.hufu.logistics.app.model.CompanyRExpress;
import me.rubick.hufu.logistics.app.repository.CompanyExpressRepository;
import me.rubick.hufu.logistics.app.repository.CompanyOrderRepository;
import me.rubick.hufu.logistics.app.repository.CompanyRExpressRepository;
import me.rubick.hufu.logistics.app.repository.CompanyRepository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class CompanyOrderService {

    @Resource
    private CompanyOrderRepository companyOrderRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private CompanyRExpressRepository companyRExpressRepository;

    @Resource
    private CompanyExpressRepository companyExpressRepository;

    @Resource
    private CompanyRepository companyRepository;


    @Resource
    private CompanyService companyService;

    @Resource
    private ExcelHelper excelHelper;

//    @Transactional(readOnly = true)
//    public Page<CompanyOrder> findAll(Pageable pageable) {
//        return companyOrderRepository.findAll(pageable);
//    }

    @Transactional(readOnly = true)
    public Page<CompanyOrder> findOrder(
            String keyword,
            String ts,
            String te,
            Integer status,
            Integer expressId,
            Pageable pageable,
            Integer companyId
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CompanyOrder> criteria = builder.createQuery(CompanyOrder.class);
        Root<CompanyOrder> personRoot = criteria.from(CompanyOrder.class);
        CriteriaQuery<CompanyOrder> select = criteria.select(personRoot);

        Date startTime = Common.generateStartTime(ts);
        Date endTime = Common.generateEndTime(te);

        List<Predicate> restrictions = new ArrayList<>();

        //多字段搜索
        Expression<String> expression = builder.concat(personRoot.<String>get("batch"), " ");
        expression = builder.concat(expression, builder.concat(personRoot.<String>get("trackingNumber"), " "));
        expression = builder.concat(expression, builder.concat(personRoot.<String>get("address"), " "));
        expression = builder.concat(expression, builder.concat(personRoot.<String>get("goodsName"), " "));
        expression = builder.concat(expression, builder.concat(personRoot.<String>get("phone"), " "));

        // 模糊查询
        if (keyword != null) {
            keyword = "%" + keyword + "%";
            restrictions.add(
                    builder.like(expression, keyword)
            );
        }


        if (expressId != null && expressId > 0) {
            restrictions.add(
                    builder.equal(personRoot.get("companyExpressId"), expressId)
            );
        }

        if (status != null) {
            restrictions.add(
                    builder.equal(personRoot.get("statusId"), status)
            );
        }

        if (companyId != -1) {
            restrictions.add(
                    builder.equal(personRoot.get("companyId"), companyId)
            );
        }

        if (startTime != null) {
            switch (status) {
                case 1:
                    restrictions.add(
                            builder.greaterThanOrEqualTo(personRoot.get("createdAt"), startTime)
                    );
                    break;
                case 2:
                    restrictions.add(
                            builder.greaterThanOrEqualTo(personRoot.get("inTime"), startTime)
                    );
                    break;
                case 3:
                    restrictions.add(
                            builder.greaterThanOrEqualTo(personRoot.get("outTime"), startTime)
                    );
                    break;

            }
//            restrictions.add(
//                    builder.or(
//                            builder.greaterThanOrEqualTo(personRoot.get("createdAt"), startTime),
//                            builder.greaterThanOrEqualTo(personRoot.get("inTime"), startTime),
//                            builder.greaterThanOrEqualTo(personRoot.get("outTime"), startTime)
//                    )
//            );
        }

        if (endTime != null) {
            switch (status) {
                case 1:
                    restrictions.add(
                            builder.lessThanOrEqualTo(personRoot.get("createdAt"), endTime)
                    );
                    break;
                case 2:
                    restrictions.add(
                            builder.lessThanOrEqualTo(personRoot.get("inTime"), endTime)
                    );
                    break;
                case 3:
                    restrictions.add(
                            builder.lessThanOrEqualTo(personRoot.get("outTime"), endTime)
                    );
                    break;
            }
//            restrictions.add(
//                    builder.or(
//                            builder.greaterThanOrEqualTo(personRoot.get("createdAt"), endTime),
//                            builder.greaterThanOrEqualTo(personRoot.get("inTime"), endTime),
//                            builder.greaterThanOrEqualTo(personRoot.get("outTime"), endTime)
//                    )
//            );
        }

        Predicate[] predArray = new Predicate[restrictions.size()];
        restrictions.toArray(predArray);

        select.where(predArray);
        // 排序
//        criteria.orderBy(builder.desc(personRoot.get("batch")), builder.desc(personRoot.get("id")));
        select.orderBy(builder.desc(personRoot.get("batch")), builder.desc(personRoot.get("id")));

        /**
         * Paging
         */
        CriteriaQuery<Long> cq = builder.createQuery(Long.class);
        cq.select(builder.count(cq.from(CompanyOrder.class)));
// Following line if commented causes [org.hibernate.hql.ast.QuerySyntaxException: Invalid path: 'generatedAlias1.enabled' [select count(generatedAlias0) from xxx.yyy.zzz.Brand as generatedAlias0 where ( generatedAlias1.enabled=:param0 ) and ( lower(generatedAlias1.description) like :param1 )]]
        entityManager.createQuery(cq);
        cq.where(predArray);
        Long count = entityManager.createQuery(cq).getSingleResult();

        List<CompanyOrder> result = entityManager.createQuery(select)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        return new PageImpl<CompanyOrder>(result, pageable, count);
    }

    @Transactional(readOnly = true)
    public CompanyOrder find(Integer id) {
        CompanyOrder order = companyOrderRepository.findOne(id);

        if (order == null) {
            throw new NoSuchElementException("Company Order [" + id + "] not found.");
        }
        return order;
    }

    public List<CompanyExpress> getAllCompanyExpress() {
        return companyRExpressRepository.findAll();
    }

    public BigDecimal getPriceByUserIdAndExpressId(Integer companyId, Integer expressId) throws Exception {
        CompanyRExpress companyRExpress = companyExpressRepository.findByCompanyIdAndExpressId(companyId, expressId);

        if (companyRExpress == null) {
            throw new Exception("无法找到 company express");
        }

        return companyRExpress.getPrice();
    }


    public CompanyOrder update(CompanyOrder companyOrder) throws BaseException {
        return this.storeCompanyOrder(companyOrder);
    }


    public Company updateCompany(Company company) {
        return companyRepository.save(company);
    }

    public void parseExcel(MultipartFile file, Integer companyId) throws BaseException {
        String batch = generateBatch();
        List<CompanyOrder> orderList = excelHelper.readTemplate(file);
        List<String> trackingNumbers = new ArrayList<>();

        for (CompanyOrder order : orderList) {
            trackingNumbers.add(order.getTrackingNumber());
            order.setCompanyId(companyId);
            order.setBatch(batch);
        }

        String[] t = new String[trackingNumbers.size()];
        trackingNumbers.toArray(t);


        StringBuilder stringBuilder = new StringBuilder();

        //check the insure
        for (CompanyOrder companyOrder : orderList) {
            if (!checkInsurance(companyOrder.getInsurance())) {
                stringBuilder.append("订单号为：" + companyOrder.getTrackingNumber() + " 中的 “保额” 不符合规则<br>");
            }

            if (stringBuilder.length() > 0) {
                stringBuilder.append("保额规则为：保额大于等于100，而且必须为100的倍数，例如：100，500,1000，若没有保额可填 0");
            }
        }

        if (stringBuilder.length() > 0) {
            throw new BaseException(stringBuilder.toString());
        }

        List<CompanyOrder> result = companyOrderRepository.findByTrackingNumberIn(t);

        if (result != null && result.size() != 0) {
            StringBuilder msg = new StringBuilder("<br>");
            for (CompanyOrder o : result) {
                msg.append(o.getTrackingNumber()).append("<br>");
            }
            throw new BaseException("含有重复订单:" + msg);
        }

        this.storeCompanyOrder(orderList);
    }

    public void parseExcelLg(MultipartFile file) throws BaseException {
        List<CompanyOrder> orderList = excelHelper.readLg(file);

        for (CompanyOrder order : orderList) {
            CompanyOrder temp = companyOrderRepository.findByTrackingNumber(order.getTrackingNumber());
            temp.setLgInfo(temp.getLgInfo() + "\n" + order.getLgInfo());
            this.storeCompanyOrder(temp);
        }
    }

    private String generateBatch() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateString = format.format(new Date());
        String batch = companyOrderRepository.getMaxBatch(dateString);

        if (batch == null) {
            return "HF" + dateString + "0001";
        } else {
            String temp = batch.substring(0, 8);
            Integer no = Integer.valueOf(batch.substring(8)) + 1;
            return temp + no;
        }
    }

    public void create(CompanyOrder companyOrder) throws BaseException {
        if (companyOrderRepository.findByTrackingNumber(companyOrder.getTrackingNumber()) != null) {
            throw new BaseException(companyOrder.getTrackingNumber() + "订单号已存在");
        }
        companyOrder.setBatch(generateBatch());
        companyOrder.setStatusId(1);
        this.update(companyOrder);
    }

    public List<Company> findAllCompany() {
        return companyRepository.findAll();
    }

    public List<CompanyOrder> findInTrackingNumber(String[] trackingNumber) {
        return companyOrderRepository.findByTrackingNumberIn(trackingNumber);
    }

    public void deleteIn(String[] trackingNumbers) {
        companyOrderRepository.deleteByTrackingNumberIn(trackingNumbers);
    }

    public void updateOrderStatusTo2(String[] trackingNumber) throws BaseException {
        List<CompanyOrder> orders = companyOrderRepository.findByTrackingNumberIn(trackingNumber);

        for (CompanyOrder o : orders) {
            if (o.getStatusId().equals(1)) {
                o.setInTime(new Date());
                o.setStatusId(2);
            }
        }

        this.storeCompanyOrder(orders);
    }

    public Double getTotalAtMonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        return companyOrderRepository.getTotalAtMonth(month);
    }

    public List<CompanyExpress> findAllCompanyExpress() {
        return companyRExpressRepository.findAll();
    }

    private void storeCompanyOrder(List<CompanyOrder> list) throws BaseException {
        for (CompanyOrder order : list) {
            this.storeCompanyOrder(order);
        }
    }

    private CompanyOrder storeCompanyOrder(CompanyOrder companyOrder) throws BaseException {
        if (!checkInsurance(companyOrder.getInsurance())) {
            throw new BaseException("订单号为：" + companyOrder.getTrackingNumber() + " 中的 “保额” 不符合规则<br>保额规则为：保额大于 100 且必须为100的倍数，例如：100，500,1000，若没有保额可填 0 ");
        }

        if (!Common.isPhone(companyOrder.getPhone())) {
            throw new BaseException("订单号为：" + companyOrder.getTrackingNumber() + " 中的 “联系人电话” 必须为11位的数字");
        }

        return companyOrderRepository.save(companyOrder);
    }

    private boolean checkInsurance(Integer insurance) {
        return insurance != null && insurance >= 0 && insurance % 100 == 0;
    }
}
