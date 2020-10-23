/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import static com.alhanah.samicalapp.MainForm.getFormRTL;
import static com.alhanah.samicalapp.SamiApplication.getStore;
import static com.alhanah.samicalapp.SamiApplication.getT;

import com.codename1.components.Accordion;
import com.codename1.payment.Purchase;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hussam
 */
public class BasketForm extends Form {
    //   CalendarStore store;

    MainForm main;
    Accordion acrd;
    List<MyItem> list;

    BasketForm(MainForm m) {
        main = m;
        //   store=getStore();

        prepareMyLayout();
    }

    public void showSKU(String sku) {
        if (sku == null) {
            return;
        }
        for (MyItem m : list) {
            if (m.getSku().equals(sku)) {
                acrd.expand(m);
            }
        }

    }

    private void prepareMyLayout() {
        this.removeAll();
        setLayout(new BorderLayout());
        list = new ArrayList<>();
        acrd = new Accordion(FontImage.MATERIAL_SHOP, FontImage.MATERIAL_REMOVE);
        //    acrd.setUIID("Label");
        acrd.setRTL(getFormRTL());
        for (String item : getStore().getItemsForSale()) {
            MyItem i = new MyItem(item);
            list.add(i);
            acrd.addContent(i.getHeader(), i);
        }
        add(BorderLayout.CENTER, acrd);
        getToolbar().addCommandToLeftBar(new Command("Back") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                main.show();

                main.animate();
            }

        });
        getToolbar().addCommandToLeftBar(new Command("Refresh") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                refreshBasket();
            }

        });
        this.revalidate();
        this.animate();
    }

    class MyItem extends Container {

        String sku;

        MyItem(String sku) {
            this.setRTL(getFormRTL());
            this.sku = sku;
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            Label l = new Label(getStore().getItemName(sku));
            l.setRTL(getFormRTL());
            add(l);
            TextArea t = new TextArea();
            t.setText(getT(getStore().getItemDescription(sku)));
            t.setEditable(false);
            t.setRTL(getFormRTL());
            add(t);
            boolean purchased = getStore().isPurchased(sku);
            if (purchased) {
                this.getAllStyles().setBgTransparency(150);
            }
            //    hussam.println("item "+sku+" "+purchased);
            if (getStore().isPurchased(sku)) {
                add(new Label("alreadyPurchased"));
            } else {
                add(new Button(new Command("buy") {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if(Purchase.getInAppPurchase().wasPurchased(sku)){
                            //hussam.println(sku+" already purchased.");
                            return;
                        }
                       // hussam.println("Purchasing: " + sku);
                        Purchase.getInAppPurchase().purchase(sku);
                        //refreshBasket();

                    }

                }));
                
            }
        }

        public String getSku() {
            return sku;
        }

        public String getHeader() {
            String x = (getStore().isPurchased(sku)) ? "purchased" : "notpurchased";
            String result = getT(x) + " " + getT(getStore().getItemName(sku)) + " ";
            //   hussam.println("Header: "+result);
            return result;
        }

    }

    private void refreshBasket() {
        getStore().updatePurchaseList();
        BasketForm form = new BasketForm(main);
        form.show();
    }
}
