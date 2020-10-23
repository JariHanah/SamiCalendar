/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import static com.alhanah.samicalapp.SamiApplication.getT;
import nasiiCalendar.locationBasid.City;
import com.alhanah.samicalapp.store.CityList;
import com.alhanah.samicalapp.store.UserDefaults;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import java.util.ArrayList;
import java.util.List;
import nasiiCalendar.locationBasid.LunerLocationCalendar;
/**
 *
 * @author hmulh
 */
public class CitySearchForm extends Form implements ActionListener<ActionEvent> {

    TextField search;
    City current;
    CityList cityList;
    public CitySearchForm(CityList list2,City cur) {
        this.current = cur;
        if(current==null & list2.getCities().size()>0)current=list2.getCities().get(0);
        cityList=list2;
            UserDefaults ud=new UserDefaults();
        
        add(new InfiniteProgress());
        setLayout(new BorderLayout());
        Container cont = new Container();
        cont.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        getToolbar().addSearchCommand(e -> {
            String text = (String) e.getSource();
            if (text == null || text.length() == 0) {
                // clear search
                for (Component cmp : cont) {
                    cmp.setHidden(true);
                    cmp.setVisible(false);
                    MyButton b=(MyButton) cmp;
                    boolean show = b.getTextLine1() != null && (current.getName().toLowerCase().equals(b.getTextLine1().toLowerCase()) | current.getArabName().equals(b.getTextLine2().toLowerCase()));
                    
                    if(show){
                        cmp.setHidden(!show);
                        cmp.setVisible(show);
                    }
                }
                
                cont.animateLayout(150);
            } else {
                text = text.toLowerCase();
                for (Component cmp : cont) {
                    MyButton mb = (MyButton) cmp;
                    City c=mb.getCity();
                    String line1 = mb.getTextLine1();
                    String line2 = mb.getTextLine2();
                    boolean show = line1 != null && (c.getName().toLowerCase().indexOf(text) > -1 | c.getArabName().indexOf(text) > -1);
                    mb.setHidden(!show);
                    mb.setVisible(show);
                }
                cont.animateLayout(150);
            }
        }, 4);

        //CityList cityList = new CityList();
        List<City> list = new ArrayList<City>(cityList.getCities());
        //List<City>listAra=new ArrayList<City>(list);
        City closeCity = cityList.getClosestCity(current.getLat(), current.getLon());
        list.add(0, closeCity);
        //List<Component> comps = new ArrayList<Component>();
        cont.setScrollableY(true);
        for (int i = 0; i < cityList.getCities().size(); i++) {
            City c = list.get(i);
            MultiButton m = new MyButton(c);
            m.setTextLine1(c.getName()+" "+c.getArabName());
            m.setTextLine2(c.getArabName());
        //    m.setTextLine3("Lat:" + c.getLat() + " Long:" + c.getLon());
            m.addActionListener(this);
            cont.addComponent(m);
        }
        getContentPane().add(BorderLayout.CENTER, cont);
        show();

    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        MyButton m = (MyButton) evt.getActualComponent();
        City c=m.getCity();
        boolean flag=Dialog.show(c.getName(), getT("defaultCity?")+" "+c.getName(), "ok", "cancel");
        if(flag){
            SamiApplication.setBasicCity(c);
            System.err.println("city: "+c);
            MainForm.getForm().updateView();
            MainForm.getForm().show();
        }
        //hussam.println("city: " + m.getCity());
    }

    class MyButton extends MultiButton {

        City city;

        public MyButton(City city) {
            this.city = city;
            setUIID("MyButton");
        }

        City getCity() {
            return city;
        }
    }
}