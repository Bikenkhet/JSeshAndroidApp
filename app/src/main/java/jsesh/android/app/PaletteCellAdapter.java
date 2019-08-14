package jsesh.android.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PaletteCellAdapter extends RecyclerView.Adapter<PaletteCellAdapter.ViewHolder> {
    private GlyphBitmap[] dataset;
    private PaletteFragment fragment;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageView;
        public RelativeLayout layout;
        public ViewHolder(RelativeLayout l) {
            super(l);
            layout = l;
            imageView = (ImageView) l.getChildAt(0);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PaletteCellAdapter(GlyphBitmap[] dataset, PaletteFragment fragment) {
        this.dataset = dataset;
        this.fragment = fragment;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public PaletteCellAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        RelativeLayout l = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_palette_cell, parent, false);
        //FIXME Scale for screen size
        l.setMinimumHeight(100);

        ImageView v = (ImageView) l.getChildAt(0);

        return new ViewHolder(l);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.imageView.setImageBitmap(dataset[position].getBitmap());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = dataset[holder.getAdapterPosition()].code;
                //Toast.makeText(holder.layout.getContext(), code, Toast.LENGTH_SHORT).show();
                fragment.updateSelectedSign(code);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.length;
    }
}