package com.fauzighozali.mgamobile.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fauzighozali.mgamobile.R;
import com.fauzighozali.mgamobile.activity.DetailBookActivity;
import com.fauzighozali.mgamobile.model.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> dataModelArrayList;
    private Context context;

    public BookAdapter(Context context, List<Book> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.activity_book_adapter, viewGroup, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i) {
        Book book = dataModelArrayList.get(i);

        Picasso.with(context).load("http://api-kms.maesagroup.co.id/files/" + book.getImage()).into(bookViewHolder.imgBook);
        bookViewHolder.titleBook.setText(book.getTitle());
        bookViewHolder.descBook.setText(book.getDescription());

        bookViewHolder.btnReadNow.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailBookActivity.class);
            intent.putExtra("title", book.getTitle());
            intent.putExtra("long_desc", book.getDescription());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (dataModelArrayList != null) ? dataModelArrayList.size() : 0;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgBook;
        private TextView titleBook, descBook;
        private Button btnReadNow;

        public BookViewHolder(View itemView) {
            super(itemView);

            imgBook = itemView.findViewById(R.id.image_view_book);
            titleBook = itemView.findViewById(R.id.text_view_title_book);
            descBook = itemView.findViewById(R.id.text_view_desc_book);
            btnReadNow = itemView.findViewById(R.id.button_book_read_now);
        }
    }

}