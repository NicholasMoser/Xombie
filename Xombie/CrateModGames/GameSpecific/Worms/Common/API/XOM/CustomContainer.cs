using System;
using System.Collections.Generic;
using System.IO;

namespace CrateModLoader.GameSpecific.WormsForts.XOM
{
    public class CustomContainer : NamedContainer
    {
        public override string Name
        {
            get { return ParentFile.Strings[(int)NameKey.Value]; }
            set { ParentFile.Strings[(int)NameKey.Value] = value; }
        }
        public VInt NameKey = new VInt();
        public uint Flags;
        public Dictionary<string, VInt> ints;
        public Dictionary<string, ByteBool> byteBools;
        public Dictionary<string, Vector2> vector2s;
        public Dictionary<string, Vector3> vector3s;
        public Dictionary<string, Vector4> vector4s;
        public Dictionary<string, Color> colors;
        public Dictionary<string, ByteColor> byteColors;
        public Dictionary<string, Matrix> matrices;

        public override void Read(BinaryReader reader)
        {
            // do nothing
        }

        public override void Write(BinaryWriter writer)
        {
            // do nothing
        }
    }
}
