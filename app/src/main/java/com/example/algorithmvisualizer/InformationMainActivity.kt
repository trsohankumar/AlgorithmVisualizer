package com.example.algorithmvisualizer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.algorithmvisualizer.databinding.ActivityInfoBinding


class InformationMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var sendProps: String

        binding.dijkstra.setOnClickListener{
            sendProps="dijkstra"
            Intent(this,InformationSpecific::class.java).also{
                it.putExtra("EXTRA_PROPS",sendProps)
                startActivity(it);
            }
        }

        binding.astar.setOnClickListener{
            sendProps="astar"
            Intent(this,InformationSpecific::class.java).also{
                it.putExtra("EXTRA_PROPS",sendProps)
                startActivity(it);
            }
        }
        binding.bfs.setOnClickListener{
            sendProps="bfs"
            Intent(this,InformationSpecific::class.java).also{
                it.putExtra("EXTRA_PROPS",sendProps)
                startActivity(it);
            }
        }
        binding.dfs.setOnClickListener{
            sendProps="dfs"
            Intent(this,InformationSpecific::class.java).also{
                it.putExtra("EXTRA_PROPS",sendProps)
                startActivity(it);
            }
        }
        binding.bubblesort.setOnClickListener{
            sendProps="bubblesort"
            Intent(this,InformationSpecific::class.java).also{
                it.putExtra("EXTRA_PROPS",sendProps)
                startActivity(it);
            }
        }
        binding.quicksort.setOnClickListener{
            sendProps="quicksort"
            Intent(this,InformationSpecific::class.java).also{
                it.putExtra("EXTRA_PROPS",sendProps)
                startActivity(it);
            }
        }
        binding.selectionsort.setOnClickListener{
            sendProps="selectionsort"
            Intent(this,InformationSpecific::class.java).also{
                it.putExtra("EXTRA_PROPS",sendProps)
                startActivity(it);
            }
        }
        binding.radixsort.setOnClickListener{
            sendProps="radixsort"
            Intent(this,InformationSpecific::class.java).also{
                it.putExtra("EXTRA_PROPS",sendProps)
                startActivity(it);
            }
        }
        binding.insertionsort.setOnClickListener{
            sendProps="insertionsort"
            Intent(this,InformationSpecific::class.java).also{
                it.putExtra("EXTRA_PROPS",sendProps)
                startActivity(it);
            }
        }
        binding.heapsort.setOnClickListener{
            sendProps="heapsort"
            Intent(this,InformationSpecific::class.java).also{
                it.putExtra("EXTRA_PROPS",sendProps)
                startActivity(it);
            }
        }
        binding.mergesort.setOnClickListener{
            sendProps="mergesort"
            Intent(this,InformationSpecific::class.java).also{
                it.putExtra("EXTRA_PROPS",sendProps)
                startActivity(it);
            }
        }
        binding.backButton.setOnClickListener {
            finish()


        }
    }
}
