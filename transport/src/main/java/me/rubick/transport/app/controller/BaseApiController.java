package me.rubick.transport.app.controller;

import me.rubick.common.app.response.RestResponse;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.model.PackageProduct;
import me.rubick.transport.app.repository.PackageRepository;
import me.rubick.transport.app.service.PayService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
public class BaseApiController extends AbstractController {

    @Resource
    private PayService payService;

    @Resource
    private PackageRepository packageRepository;

    @RequestMapping("/api/base/u2r")
    public RestResponse<BigDecimal> r2u(
            @RequestParam BigDecimal value
    ) {
        BigDecimal u2r = new BigDecimal(configService.findOneByKey("U2R"));

        BigDecimal result = value.multiply(u2r);

        return new RestResponse<>(result);
    }

    @RequestMapping(value = "/api/base/{id}/calc_RK", method = RequestMethod.POST)
    public RestResponse<BigDecimal> calcRK(
            @PathVariable long id,
            @RequestParam("qty[]") List<Integer> qty,
            @RequestParam("p[]") List<Long> pIds
    ) {
        Package p = packageRepository.findOne(id);
        if (pIds.size() != qty.size()) {
            return new RestResponse<>("");
        }
        if (p.getPackageProducts().size() != pIds.size()) {
            return new RestResponse<>("");
        }
        int count = pIds.size();
        for (PackageProduct pp : p.getPackageProducts()) {
            for (int i = 0; i < count; i++) {
                if (pp.getProductId() == pIds.get(i)) {
                    pp.setQuantity(qty.get(i));
                }
            }
        }
        return new RestResponse<>(payService.calcCK(p).getTotal());
    }
}
