package me.rubick.hufu.logistics.app.service;

import me.rubick.hufu.logistics.app.model.CompanyOrder;
import me.rubick.hufu.logistics.app.model.Waybill;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import me.rubick.hufu.logistics.app.exception.BaseException;
import me.rubick.hufu.logistics.app.library.helper.*;


import java.io.OutputStream;
import java.util.List;

@Service
public class ExcelHelper {
    static {
        eExcelWriter = new EExcelWriter();
        guangZhuoExcelWriter = new GuangZhuoExcelWriter();
        lgExcelWriter = new LgExcelWriter();
        templateExcelReader = new TemplateExcelReader();
        lgExcelReader = new LgExcelReader();
        waybillWriter = new WaybillWriter();
        waybillReader = new WaybillReader();
        defaultExcelWriter = new DefaultExcelWriter();
    }

    private static EExcelWriter eExcelWriter;

    private static DefaultExcelWriter defaultExcelWriter;

    private static GuangZhuoExcelWriter guangZhuoExcelWriter;

    private static LgExcelWriter lgExcelWriter;

    private static TemplateExcelReader templateExcelReader;

    private static LgExcelReader lgExcelReader;

    private static WaybillWriter waybillWriter;
    private static WaybillReader waybillReader;

    public void writeE(List<CompanyOrder> orders, OutputStream outputStream) throws BaseException {
        eExcelWriter.write(orders, outputStream);
    }

    public void writeGuangZhuo(List<CompanyOrder> orders, OutputStream outputStream) throws BaseException {
        guangZhuoExcelWriter.write(orders, outputStream);
    }

    public void writeLg(List<CompanyOrder> orders, OutputStream outputStream) throws BaseException {
        lgExcelWriter.write(orders, outputStream);
    }

    public List<CompanyOrder> readTemplate(MultipartFile file) throws BaseException {
        return templateExcelReader.read(file);
    }

    public List<CompanyOrder> readLg(MultipartFile file) throws BaseException {
        return lgExcelReader.read(file);
    }

    public void writeWaybill(List<Waybill> waybills, OutputStream outputStream)
            throws BaseException {
        waybillWriter.write(waybills, outputStream);
    }

    public List<Waybill> readWaybill(MultipartFile file) throws BaseException {
        return waybillReader.read(file);
    }

    public void writeDefault(List<CompanyOrder> orders, OutputStream outputStream) throws BaseException {
        defaultExcelWriter.write(orders, outputStream);
    }
}
