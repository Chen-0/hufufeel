package me.rubick.transport.app.controller.admin;

import me.rubick.transport.app.controller.AbstractController;
import me.rubick.transport.app.model.SwitchSku;
import me.rubick.transport.app.repository.SwitchSkuRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class AdminStockController extends AbstractController {

    @Resource
    private SwitchSkuRepository switchSkuRepository;

    @RequestMapping(value = "/admin/switch_sku/index", method = RequestMethod.GET)
    public String indexSwitchSku(Model model) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        List<SwitchSku> switchSkus = switchSkuRepository.findAll(sort);

        model.addAttribute("elements", switchSkus);

        return "/admin/store/switch_sku_index";
    }
}
