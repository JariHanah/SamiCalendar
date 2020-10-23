/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import static com.alhanah.samicalapp.SamiApplication.getStore;
import nasiiCalendar.locationBasid.City;
import com.codename1.components.Accordion;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import nasiiCalendar.BasicCalendar;
import nasiiCalendar.locationBasid.LunerLocationCalendar;

/**
 *
 * @author Hussam
 */
public class CalendarChooserDialog extends Dialog {

    CalendarListRequester controller;
    //   List<BasicCalendar> original = new ArrayList<BasicCalendar>();
    List<CalendarChooser> list = new ArrayList<>();
    Accordion cord;
    int max = 0;

    public CalendarChooserDialog(CalendarListRequester controller) {
        this(controller, 10);
    }

    public CalendarChooserDialog(CalendarListRequester controller, int max) {
        this.controller = controller;
        this.max = max;
        //   list.addAll(controller.getCalendars());
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        try {
            cord = new Accordion('o', 'c', "OpenCloseUIID");
        } catch (Exception e) {
            Dialog.show("501" + e.getMessage(), e.getMessage(), "o", "ok");

            e.printStackTrace();
        }

        try {
            prepareLayout();
        } catch (Exception e) {
            Dialog.show("503" + e.getMessage(), e.getMessage(), "o", "ok");

            e.printStackTrace();
        }

        try {
            this.setDisposeWhenPointerOutOfBounds(true);
        } catch (Exception e) {
            Dialog.show("505" + e.getMessage(), e.getMessage(), "o", "ok");

            e.printStackTrace();

        }
    }

    private void addCalendar(BasicCalendar bc) {
        try{
        if (bc == null) {
            bc = getStore().getCalendar(BasicCalendar.SAMI_FIXED_ID);
            if(bc==null){
                 Dialog.show("800 bc is null", "WHAT", "o","ok");
            }
        }
        }catch(Exception e){
            Dialog.show("710"+e.getMessage(), e.getMessage(), "o","ok");
            
            e.printStackTrace();
        }
        CalendarChooser cc=null;
        try{
        cc = new CalendarChooser(bc);
        }catch(Exception e){
            Dialog.show("720"+e.getMessage(), e.getMessage(), "o","ok");
            
            e.printStackTrace();
        }
        try{
        Container header = new Container(new BoxLayout(BoxLayout.X_AXIS));
        header.add(new Label(cc.getCalendar().getName()));
        
        cord.addContent(bc.getName(), cc);
        list.add(cc);
        }catch(Exception e){
            Dialog.show("740"+e.getMessage(), e.getMessage(), "o","ok");
            
            e.printStackTrace();
        }
    }

    private void prepareLayout() {
        try {
            try{
            cord.removeAll();
            }catch(Exception e){
            Dialog.show("610"+e.getMessage(), e.getMessage(), "o","ok");
            
            e.printStackTrace();
        }
            try{
            cord.addOnClickItemListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    // if(!(arg0.getActualComponent() instanceof Button))
                    growOrShrink();
                }
            });
            }catch(Exception e){
            Dialog.show("620"+e.getMessage(), e.getMessage(), "o","ok");
            
            e.printStackTrace();
        }
            int x=0;
            try{
            for (BasicCalendar bd : controller.getCalendars()) {
                x++;
                addCalendar(bd);
            }
            }catch(Exception e){
            Dialog.show("630+ "+x+" " +e.getMessage(), e.getMessage(), "o","ok");
            
            e.printStackTrace();
        }
            if (list.size() == 0) {
                //addNewCalendarItem(CalendarFactory.getSamiCalendar(),-1);
                addCalendar(null);
            }
        } catch (Exception e) {
            Dialog.show("510" + e.getMessage(), e.getMessage(), "o", "ok");

            e.printStackTrace();
        }
        Container c = null;
        Button cancel = null, add = null, apply = null;
        try {
            c = new Container(new BoxLayout(BoxLayout.X_AXIS));
            cancel = new Button("cancel");
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    CalendarChooserDialog.this.dispose();
                }
            });
            add = new Button("addcalendar");
            add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (list.size() >= max) {
                        Dialog.show("limit", "limitmessage", new Command("ok"));
                        return;
                    }
                    addCalendar(null);
                    growOrShrink();
                    cord.animateLayout(200);
                }
            });
            apply = new Button("ok");
            apply.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    controller.setCalendars(getSelectedCalendars());
                    CalendarChooserDialog.this.dispose();
                }
            });
        } catch (Exception e) {
            Dialog.show("520" + e.getMessage(), e.getMessage(), "o", "ok");

            e.printStackTrace();
        }
        try {
            c.add(cancel);
            //c.add(add);
            c.add(apply);
            add(c);
            add(cord);
            add(add);
        } catch (Exception e) {
            Dialog.show("530" + e.getMessage(), e.getMessage(), "o", "ok");

            e.printStackTrace();
        }
    }

    public List<BasicCalendar> getSelectedCalendars() {
        List<BasicCalendar> r = new ArrayList<>();
        for (CalendarChooser cc : list) {
            r.add(cc.getCalendar());
        }
        return r;
    }

    private void removeChooser(CalendarChooser cc) {

        cord.removeContent(cc);
        cord.collapse(cc);
        list.remove(cc);
        if (list.size() == 0) {
            addCalendar(null);
        }
        cord.animateLayout(200);

    }
    static int x=0;
    class CalendarChooser extends Container {

        BasicCalendar chosen;
        CalendarChooser(BasicCalendar bc) {
            x++;
            chosen = bc;
            setLayout(new FlowLayout());
            Button remove = new Button("remove");
            try{
            remove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    CalendarChooser c1 = CalendarChooser.this;
                    if (list.size() == 1) {
                        return;
                    }
                    int x = list.indexOf(c1);
                    int position = 0;
                    if (x == 0) {
                        position = 1;
                    }
                    if (x == list.size() - 1) {
                        position = list.size() - 2;
                    }
                    position = Math.min(list.size() - 1, x);
                    position = Math.max(0, position);
                    cord.expand(list.get(position));
                    removeChooser(CalendarChooser.this);
                    cord.animateLayout(200);
                }
            });
            }catch (Exception e) {
            Dialog.show("920" + e.getMessage(), e.getMessage(), "o", "ok");

            e.printStackTrace();
        }
            List<BasicCalendar> cals= getStore().getCalendars();
             
            add(remove);
            City city= SamiApplication.getBasicLocation();
            if(city!=null){
            cals.add(new LunerLocationCalendar(city));
            }
            try{
            for (BasicCalendar b1 : cals) {
                Button cal = new Button(b1.getName());
                add(cal);
                cal.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        chosen = b1;
                        CalendarChooser c1 = CalendarChooser.this;
                        int x = list.indexOf(c1);
                        cord.setHeader(chosen.getName(), CalendarChooser.this);
                        cord.collapse(CalendarChooser.this);
                        try {
                            cord.expand(list.get(x + 1));
                        } catch (IndexOutOfBoundsException e) {
                            System.err.println("Opps");
                            //   e.printStackTrace();
                        }
                        //   cord.animateLayout(200);
                        //     hussam.println("count: "+list.size());
                        //      CalendarChooserDialog.this.invalidate();
                        cord.animateLayout(200);
                    }
                });
            }
}catch (Exception e) {
            Dialog.show("940" + e.getMessage(), e.getMessage(), "o", "ok");

            e.printStackTrace();
        }
            }

        public BasicCalendar getCalendar() {
            return chosen;
        }

    }

}
