package dmitribak.taxi.taxidemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class Forgot_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.Forgot_pass_title);
        setContentView(R.layout.activity_get_password);



///////////////////////////////////////////////////////////////
//Ввод номера телефона
        init(R.id.forgot_editText_phone, new ExampleBehaviour() {
            @Override
            void changeMask(MaskImpl mask) {
                mask.setHideHardcodedHead(true);
            }

        });
///////////////////////////////////////////////////////////////
    }

    public void Get_pass(View view) {
        Button get_pass = (Button) findViewById(R.id.Forgot_get_pass);
        Intent intent = new Intent(Forgot_password.this, Input_pasword.class);
        startActivity(intent);
    }



///////////////////////////////////////////////////////////////
//Ввод номера телефона
    public void init(int editTextId, ExampleBehaviour behaviour) {
        EditText editText = (EditText) findViewById(editTextId);
        Slot[] slots = Slot.copySlotArray(PredefinedSlots.RUS_PHONE_NUMBER);
        behaviour.changeSlots(slots);
        MaskImpl mask = new MaskImpl(slots, behaviour.isTerminated());
        behaviour.changeMask(mask);
        MaskFormatWatcher watcher = new MaskFormatWatcher(mask);
        if (behaviour.fillWhenInstall()) {
            watcher.installOnAndFill(editText);
        } else {
            watcher.installOn(editText);
        }
    }

    class ExampleBehaviour {
        void changeMask(MaskImpl mask) {
        }

        void changeSlots(Slot[] slots) {
        }

        boolean isTerminated() {
            return true;
        }

        boolean fillWhenInstall() {
            return false;
        }
    }
//Ввод номера телефона
///////////////////////////////////////////////////////////////

}
