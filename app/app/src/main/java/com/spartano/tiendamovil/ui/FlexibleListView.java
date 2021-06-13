package com.spartano.tiendamovil.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

// Esta clase fué creada para poder asignar "wrap_content" a layout_height o layout_width a un elemento ListView para que no se aplique automaticamente un scroll.
// Es necesario principalmente en fragment_inicio, donde el ListView de recomendaciones esta abajo de otros elementos, y en dispositivos con pantallas chicas no se podía scrollear.
public class FlexibleListView extends ListView {

    public FlexibleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlexibleListView(Context context) {
        super(context);
    }

    public FlexibleListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
