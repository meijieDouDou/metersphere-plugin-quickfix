package io.metersphere.plugin.quickfix.sampler;


import cicc.quickfix.client.FixInitiatorApplication;
import cicc.quickfix.client.jmeter.QuickFixSampler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import io.metersphere.plugin.core.MsParameter;
import io.metersphere.plugin.core.MsTestElement;
import io.metersphere.plugin.core.utils.LogUtil;
import io.metersphere.plugin.quickfix.utils.ElementUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.collections.HashTree;
import org.apache.log4j.Logger;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MsQuickFixSampler extends MsTestElement {
    public MsQuickFixSampler() {

    }
    private static final Logger LogUtil = Logger.getLogger(FixInitiatorApplication.class);
    private String clazzName = "io.metersphere.plugin.quickfix.sampler.MsQuickFixSampler";

    @JSONField(ordinal = 20)
    private String serverIp;
    @JSONField(ordinal = 21)
    private String serverPort;
    @JSONField(ordinal = 22)
    private String requestParam;
    @JSONField(ordinal = 23)
    private String fileName;
    @JSONField(ordinal = 24)
    private String requstMsg;
    @JSONField(ordinal = 25)
    private File message;
    @JSONField(ordinal = 26)
    private File properties;
    @JSONField(ordinal = 27)
    private File FIX42;


    @Override
    public void toHashTree(HashTree tree, List<MsTestElement> hashTree, MsParameter config) {
        LogUtil.info("===========开始转换MsQuickFixSampler ==================");
        if (!this.isEnable()) {
            return;
        }
        final HashTree pluginTree = tree.add(initQuickFixSample(config));
        if (CollectionUtils.isNotEmpty(hashTree)) {
            for (MsTestElement el : hashTree) {
                el.toHashTree(pluginTree, el.getHashTree(), config);
            }
        }
    }

    public QuickFixSampler initQuickFixSample(MsParameter config) {
        try {
            QuickFixSampler quickfixSampler = new QuickFixSampler();

            // base 执行时需要的参数
            quickfixSampler.setProperty("MS-ID", this.getId());
            LogUtil.info("===MS-ID:"+this.getId());

            String indexPath = this.getIndex();
            quickfixSampler.setProperty("MS-RESOURCE-ID", this.getResourceId() + "_" + ElementUtil.getFullIndexPath(this.getParent(), indexPath));
            LogUtil.info("===MS-RESOURCE-ID:"+this.getResourceId() + "_" + ElementUtil.getFullIndexPath(this.getParent(), indexPath));
            List<String> id_names = new LinkedList<>();
            ElementUtil.getScenarioSet(this, id_names);
            quickfixSampler.setProperty("MS-SCENARIO", JSON.toJSONString(id_names));
            LogUtil.info("===MS-SCENARIO:"+JSON.toJSONString(id_names));

            // 自定义插件参数转换
            quickfixSampler.setEnabled(this.isEnable());
            quickfixSampler.setName(StringUtils.isNotEmpty(this.getName()) ? this.getName() : "QuickFixSampler");

            quickfixSampler.setProperty(TestElement.GUI_CLASS, "cicc.quickfix.client.jmeter.QuickFixSamplerUI");
            quickfixSampler.setProperty(TestElement.TEST_CLASS, "cicc.quickfix.client.jmeter.QuickFixSampler");

            quickfixSampler.setProperty("server_ip", this.getServerIp());
            quickfixSampler.setProperty("port", this.getServerPort());
            quickfixSampler.setProperty("request_param", this.getRequestParam());;
            quickfixSampler.setProperty("fileName", this.getFileName());
            quickfixSampler.setProperty("RESULT_CLASS", "org.apache.jmeter.samplers.SampleResult");
            quickfixSampler.setProperty("requstMsg", quickfixSampler.getApplication().getRequstMsg().toString());


            LogUtil.info("quickfixSampler 转换后的内容为：" + JSON.toJSONString(quickfixSampler));
           return quickfixSampler;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
